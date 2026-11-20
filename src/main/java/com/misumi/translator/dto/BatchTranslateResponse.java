package com.misumi.translator.dto;

import java.util.List;

public record BatchTranslateResponse(
        List<TranslateResponse> items,
        long elapsedMs
) {}
