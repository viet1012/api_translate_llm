package com.misumi.translator.dto;

public record TranslateResponse(
        String source,
        String translation,
        String targetLang,
        String model,
        int totalTokens,
        long elapsedMs
) {}
