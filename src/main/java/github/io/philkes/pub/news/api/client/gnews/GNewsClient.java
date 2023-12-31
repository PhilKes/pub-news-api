package github.io.philkes.pub.news.api.client.gnews;

import github.io.philkes.pub.news.api.client.gnews.dto.Attribute;
import github.io.philkes.pub.news.api.client.gnews.dto.Category;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import github.io.philkes.pub.news.api.client.gnews.dto.SortBy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.time.Instant;

import static github.io.philkes.pub.news.api.PubNewsApiApplication.CACHE_ARTICLES;
import static github.io.philkes.pub.news.api.PubNewsApiApplication.CACHE_TOP_HEADLINES;

/**
 * HTTP Client for accessing GNews API to fetch news articles
 */
public interface GNewsClient {
    /**
     * Search news articles with multiple parameters.
     * For further details see <a href="https://gnews.io/docs/v4#search-endpoint-query-parameters">gnews.io/docs</a>
     *
     * @param q      search query (parameter {@code in} sets which attribute the keywords are searched for)
     * @param from   only return articles which {@link  github.io.philkes.pub.news.api.client.gnews.dto.Article#publishedAt()} is equal or after the given Instant
     * @param to     only return articles which {@link  github.io.philkes.pub.news.api.client.gnews.dto.Article#publishedAt()} is equal or before the given Instant
     * @param max    specifies how many articles should be returned at maximum
     * @param sortby specifies which attribute the articles should be sorted by
     * @param in     specifies which attribute the keywords are searched for
     * @return Entire amount of found articles and the article objects
     */
    @GetExchange("/search")
    @Cacheable(CACHE_ARTICLES)
    SearchResponse searchArticles(@RequestParam String q,
                                  @RequestParam(required = false) Instant from,
                                  @RequestParam(required = false) Instant to,
                                  @RequestParam(required = false) Long max,
                                  @RequestParam(required = false) SortBy sortby,
                                  @RequestParam(required = false) Attribute in,
                                  @RequestParam String apikey
    );

    /**
     * Search top-headlines articles.
     * For further details see <a href="https://gnews.io/docs/v4#top-headlines-endpoint">gnews.io/docs</a>
     *
     * @param category specifies the articles' categories that should be searched
     * @param q        search query for keywords search
     * @param from     only return articles which {@link  github.io.philkes.pub.news.api.client.gnews.dto.Article#publishedAt()} is equal or after the given Instant
     * @param to       only return articles which {@link  github.io.philkes.pub.news.api.client.gnews.dto.Article#publishedAt()} is equal or before the given Instant
     * @param max      specifies how many articles should be returned at maximum
     * @return Entire amount of found articles and the article objects
     */
    @GetExchange("/top-headlines")
    @Cacheable(CACHE_TOP_HEADLINES)
    SearchResponse searchTopHeadlines(@RequestParam(required = false) Category category,
                                      @RequestParam(required = false) String q,
                                      @RequestParam(required = false) Instant from,
                                      @RequestParam(required = false) Instant to,
                                      @RequestParam(required = false) Long max,
                                      @RequestParam String apikey
    );


}
