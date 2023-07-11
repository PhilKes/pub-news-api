package github.io.philkes.pub.news.api.web;

import github.io.philkes.pub.news.api.client.gnews.GNewsClient;
import github.io.philkes.pub.news.api.client.gnews.dto.*;
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

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(@RequestParam String q,
                                                        @RequestParam(required = false) Instant from,
                                                        @RequestParam(required = false) Instant to,
                                                        @RequestParam(required = false) Long max,
                                                        @RequestParam(required = false) SortBy sortBy,
                                                        @RequestParam(required = false) Attribute in) {
        SearchResponse searchResponse=gNewsClient.searchArticles(q, from, to, max, sortBy, in);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HEADER_TOTAL_ELEMENTS, String.valueOf(searchResponse.totalArticles()))
                .body(searchResponse.articles());
    }

    @GetMapping("/top-headlines")
    public ResponseEntity<List<Article>> searchTopHeadlines(@RequestParam(required = false) Category category,
                                                            @RequestParam(required = false) String q,
                                                            @RequestParam(required = false) Instant from,
                                                            @RequestParam(required = false) Instant to,
                                                            @RequestParam(required = false) Long max) {
        SearchResponse searchResponse=gNewsClient.searchTopHeadlines(category, q, from, to, max);
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
