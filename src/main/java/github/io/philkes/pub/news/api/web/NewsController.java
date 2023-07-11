package github.io.philkes.pub.news.api.web;

import github.io.philkes.pub.news.api.client.gnews.GNewsClient;
import github.io.philkes.pub.news.api.client.gnews.dto.Article;
import github.io.philkes.pub.news.api.client.gnews.dto.Attributes;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import github.io.philkes.pub.news.api.client.gnews.dto.SortBy;
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

    public NewsController(GNewsClient gNewsClient) {
        this.gNewsClient=gNewsClient;
    }

    @GetMapping
    public ResponseEntity<List<Article>> searchArticles(@RequestParam String q,
                                                        @RequestParam(required = false) Instant from,
                                                        @RequestParam(required = false) Instant to,
                                                        @RequestParam(required = false) Long max,
                                                        @RequestParam(required = false) SortBy sortBy,
                                                        @RequestParam(required = false) Attributes in,
                                                        @RequestParam String apiKey) {
        SearchResponse searchResponse=gNewsClient.searchArticles(q, from, to, max, sortBy, in, apiKey);
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
