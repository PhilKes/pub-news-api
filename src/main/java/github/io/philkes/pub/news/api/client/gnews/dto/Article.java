package github.io.philkes.pub.news.api.client.gnews.dto;

import java.time.Instant;

public record Article(
        String title,
        String description,
        String content,
        String url,
        String image,
        Instant publishedAt,
        Source source
) {
}
