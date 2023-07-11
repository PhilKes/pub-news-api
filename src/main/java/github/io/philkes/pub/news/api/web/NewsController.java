package github.io.philkes.pub.news.api.web;

import github.io.philkes.pub.news.api.client.gnews.GNewsClient;
import github.io.philkes.pub.news.api.client.gnews.GNewsClientProperties;
import github.io.philkes.pub.news.api.client.gnews.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    public static final String HEADER_TOTAL_ELEMENTS="total";
    private final GNewsClient gNewsClient;
    private final GNewsClientProperties gNewsClientProperties;

    public NewsController(GNewsClient gNewsClient, GNewsClientProperties gNewsClientProperties) {
        this.gNewsClient=gNewsClient;
        this.gNewsClientProperties=gNewsClientProperties;
    }

    @Operation(description = "Search GNews articles")
    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(@Parameter(description = "Keywords query", required = true)
                                                        @RequestParam String q,
                                                        @RequestParam(required = false)
                                                        @Parameter(description = "Search articles that were published after this Instant")
                                                        Instant from,
                                                        @Parameter(description = "Search articles that were published before this Instant")
                                                        @RequestParam(required = false)
                                                        Instant to,
                                                        @Parameter(description = "Specify maximum amount of returned articles")
                                                        @RequestParam(required = false)
                                                        Long max,
                                                        @Parameter(description = "Specify which attribute the articles should be sorted by")
                                                        @RequestParam(required = false) SortBy sortBy,
                                                        @Parameter(description = "Specify which attribute the keywords are searched for")
                                                        @RequestParam(required = false) Attribute in) {
        SearchResponse searchResponse=gNewsClient.searchArticles(q, from, to, max, sortBy, in, gNewsClientProperties.apiKey);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HEADER_TOTAL_ELEMENTS, String.valueOf(searchResponse.totalArticles()))
                .body(searchResponse.articles());
    }

    @Operation(description = "Search GNews top-headlines")
    @GetMapping("/top-headlines")
    public ResponseEntity<List<Article>> searchTopHeadlines(
            @Parameter(description = "Specify the article category to be searched")
            @RequestParam(required = false)
            Category category,
            @Parameter(description = "Keywords query")
            @RequestParam(required = false)
            String q,
            @Parameter(description = "Search articles that were published after this Instant")
            @RequestParam(required = false)
            Instant from,
            @Parameter(description = "Search articles that were published before this Instant")
            @RequestParam(required = false)
            Instant to,
            @Parameter(description = "Specify maximum amount of returned articles")
            @RequestParam(required = false)
            Long max) {
        SearchResponse searchResponse=gNewsClient.searchTopHeadlines(category, q, from, to, max, gNewsClientProperties.apiKey);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HEADER_TOTAL_ELEMENTS, String.valueOf(searchResponse.totalArticles()))
                .body(searchResponse.articles());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(WebClientResponseException exception) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }

}
