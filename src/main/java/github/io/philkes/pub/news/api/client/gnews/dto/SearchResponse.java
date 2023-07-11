package github.io.philkes.pub.news.api.client.gnews.dto;

import java.util.List;

public record SearchResponse(
        Long totalArticles,
        List<Article> articles
) {
}
