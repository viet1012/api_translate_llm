package com.misumi.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslateRequest(
        @NotBlank(message = "text is required")
        @Size(max = 5000, message = "text must be <= 5000 characters")
        String text,
        String sourceLang,   // vd: "Vietnamese"; null = tự nhận diện
        String targetLang    // vd: "Japanese"; null = default trong application.yml
) {}
