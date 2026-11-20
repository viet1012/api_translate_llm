package com.misumi.translator.controller;

import com.misumi.translator.dto.*;
import com.misumi.translator.service.TranslateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
public class TranslateController {

    private final TranslateService translateService;

    /** Dịch 1 đoạn văn bản. */
    @PostMapping
    public ResponseEntity<TranslateResponse> translate(@Valid @RequestBody TranslateRequest req) {
        return ResponseEntity.ok(translateService.translate(req));
    }

    /** Dịch nhiều đoạn (tối đa 50). */
    @PostMapping("/batch")
    public ResponseEntity<BatchTranslateResponse> batch(@Valid @RequestBody BatchTranslateRequest req) {
        return ResponseEntity.ok(translateService.translateBatch(req));
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
