# pub-news-api
Public news api for fetching News articles

# Dependencies
- JDK 17
- Spring Boot 3
- [GNews API](https://gnews.io/)

# Build and Run

1. Build .jar file:
```shell
./mvnw clean package
```

2. Run .jar file:
```shell
java -jar .\target\pub-news-api-0.0.1-SNAPSHOT.jar
```

# HTTP API
The exposed API endpoints are documented via OpenAPI at:

http://localhost:8080/v3/api-docs

Swagger-UI is embedded at:

http://localhost:8080/swagger-ui/index.html

# Environment Variables
```properties
gnews.api.baseUrl=https://gnews.io/api/v4
# API Key for the GNews API to fetch articles
gnews.api.key=<api-key>
```