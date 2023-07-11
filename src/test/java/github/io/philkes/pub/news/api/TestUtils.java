package github.io.philkes.pub.news.api;

import github.io.philkes.pub.news.api.client.gnews.dto.Article;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import github.io.philkes.pub.news.api.client.gnews.dto.Source;

import java.time.Instant;
import java.util.stream.IntStream;

public class TestUtils {

    public static SearchResponse createSearchResponse(Long total, int max) {
        return new SearchResponse(total, IntStream.range(1, max).mapToObj(TestUtils::createArticle).toList());
    }

    private static Article createArticle(int idx) {
        return new Article(
                "Title%d".formatted(idx),
                "Description%d".formatted(idx),
                "Content%d".formatted(idx),
                "Url%d".formatted(idx),
                "Image%d".formatted(idx),
                Instant.now(),
                new Source("Source%d".formatted(idx), "URI%d".formatted(idx))
        );
    }
}
