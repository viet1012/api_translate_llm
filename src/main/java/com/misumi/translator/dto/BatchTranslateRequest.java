package com.misumi.translator.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BatchTranslateRequest(
        @NotEmpty(message = "texts is required")
        @Size(max = 50, message = "max 50 items per request")
        List<String> texts,
        String sourceLang,
        String targetLang
) {}
