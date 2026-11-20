package com.misumi.translator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.misumi.translator.config.LmStudioProperties;
import com.misumi.translator.dto.*;
import com.misumi.translator.exception.LlmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateService {

    private static final String SYSTEM_PROMPT = """
            You are a professional translator for a manufacturing company (factory, safety, machines, production).
            Translate the user's text from %s to %s.
            Rules:
            - Output ONLY the translated text.
            - No quotes, no brackets, no explanation, no romanization.
            - Keep numbers, codes, machine names and units unchanged.
            """;

    private final RestClient lmStudioClient;
    private final LmStudioProperties props;

    public TranslateResponse translate(TranslateRequest req) {
        return translateOne(req.text(), req.sourceLang(), req.targetLang());
    }

    public BatchTranslateResponse translateBatch(BatchTranslateRequest req) {
        long start = System.currentTimeMillis();
        List<TranslateResponse> items = new ArrayList<>();
        for (String text : req.texts()) {
            if (!StringUtils.hasText(text)) {
                items.add(new TranslateResponse(text, "", resolveTarget(req.targetLang()), props.model(), 0, 0));
                continue;
            }
            items.add(translateOne(text, req.sourceLang(), req.targetLang()));
        }
        return new BatchTranslateResponse(items, System.currentTimeMillis() - start);
    }

    private TranslateResponse translateOne(String text, String sourceLang, String targetLang) {
        long start = System.currentTimeMillis();
        String target = resolveTarget(targetLang);
        String source = StringUtils.hasText(sourceLang) ? sourceLang : "the detected language";

        Map<String, Object> body = Map.of(
                "model", props.model(),
                "temperature", 0,
                "stream", false,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT.formatted(source, target)),
                        Map.of("role", "user", "content", text)
                )
        );

        JsonNode res;
        try {
            res = lmStudioClient.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientException e) {
            log.error("LM Studio call failed: {}", e.getMessage());
            throw new LlmException("Cannot reach LM Studio: " + e.getMessage(), e);
        }

        if (res == null || !res.path("choices").isArray() || res.path("choices").isEmpty()) {
            throw new LlmException("Invalid response from LM Studio");
        }

        String content = res.path("choices").path(0).path("message").path("content").asText("");
        String translation = clean(content);
        if (translation.isEmpty()) {
            throw new LlmException("Model returned empty translation");
        }

        int tokens = res.path("usage").path("total_tokens").asInt(0);
        long elapsed = System.currentTimeMillis() - start;
        log.info("Translated {} chars -> {} in {} ms ({} tokens)", text.length(), target, elapsed, tokens);

        return new TranslateResponse(text, translation, target, props.model(), tokens, elapsed);
    }

    private String resolveTarget(String targetLang) {
        return StringUtils.hasText(targetLang) ? targetLang : props.defaultTargetLang();
    }

    /** Bỏ ngoặc/quote thừa model hay thêm vào đầu-cuối. */
    private String clean(String s) {
        return s.trim()
                .replaceAll("^[「『\"“'`]+", "")
                .replaceAll("[」』\"”'`]+$", "")
                .trim();
    }
}
