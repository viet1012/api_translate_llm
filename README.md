# lm-translate-api

Spring Boot 3.3 / Java 17 API dịch văn bản qua LM Studio (openai/gpt-oss-20b).

## Chạy
```
mvn spring-boot:run
```
Cấu hình LM Studio trong `src/main/resources/application.yml` (port mặc định 8085).

## API
### POST /api/translate
```json
{ "text": "Thay thế cover an toàn", "sourceLang": "Vietnamese", "targetLang": "Japanese" }
```
Response:
```json
{ "source": "Thay thế cover an toàn", "translation": "安全カバーを交換する",
  "targetLang": "Japanese", "model": "openai/gpt-oss-20b", "totalTokens": 234, "elapsedMs": 812 }
```

### POST /api/translate/batch
```json
{ "texts": ["Thay thế cover an toàn", "Kiểm tra dây điện"], "sourceLang": "Vietnamese", "targetLang": "Japanese" }
```

### GET /api/translate/health
"# api_translate_llm" 
