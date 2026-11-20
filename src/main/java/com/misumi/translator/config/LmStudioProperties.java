package com.misumi.translator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lmstudio")
public record LmStudioProperties(
        String baseUrl,
        String model,
        int connectTimeoutSeconds,
        int readTimeoutSeconds,
        String defaultTargetLang
) {}
