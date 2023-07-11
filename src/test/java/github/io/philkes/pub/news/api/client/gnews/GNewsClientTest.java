package github.io.philkes.pub.news.api.client.gnews;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.io.philkes.pub.news.api.PubNewsApiApplication;
import github.io.philkes.pub.news.api.TestUtils;
import github.io.philkes.pub.news.api.client.gnews.dto.Article;
import github.io.philkes.pub.news.api.client.gnews.dto.Category;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.Configuration;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.util.TestSocketUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@ContextConfiguration(
        initializers = GNewsClientTest.PropertyOverrideContextInitializer.class,
        classes = PubNewsApiApplication.class)
class GNewsClientTest {

    private static final String SERVER_ADDRESS="localhost";

    private static int serverPort=TestSocketUtils.findAvailableTcpPort();
    private static ClientAndServer mockServer;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GNewsClient gNewsClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeAll
    static void startServer() {
        Configuration config=Configuration.configuration().logLevel(Level.WARN);
        mockServer=startClientAndServer(config, serverPort);
    }

    @AfterEach
    void clearCache() {
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }


    @Test
    void searchArticlesFound() throws IOException {
        SearchResponse expected=TestUtils.createSearchResponse(1000L, 10);
        mockSearchResponse("/search", objectMapper.writeValueAsString(expected));
        SearchResponse actual=gNewsClient.searchArticles("testquery", null, null, null, null, null, "test-apikey");
        assertEqualSearchResults(expected, actual);
    }

    @Test
    void searchArticlesEmpty() throws IOException {
        SearchResponse expected=new SearchResponse(0L, List.of());
        mockSearchResponse("/search", objectMapper.writeValueAsString(expected));
        SearchResponse actual=gNewsClient.searchArticles("testquery", null, null, null, null, null, "test-apikey");
        assertEqualSearchResults(expected, actual);
    }

    @Test
    void searchArticlesCaching() throws IOException {
        SearchResponse expected=TestUtils.createSearchResponse(1000L, 10);
        mockSearchResponse("/search", objectMapper.writeValueAsString(expected));
        for(int i=0; i<5; i++) {
            SearchResponse actual=gNewsClient.searchArticles("testquery", null, null, null, null, null, "test-apikey");
            assertEqualSearchResults(expected, actual);
        }
    }

    @Test
    void searchArticlesRequiredParameters() {
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> {
            gNewsClient.searchArticles(null, null, null, null, null, null, "test-apikey");
        });
        assertTrue(exception.getMessage().contains("'q'"));
    }

    @Test
    void searchTopHeadlinesFound() throws IOException {
        SearchResponse expected=TestUtils.createSearchResponse(1000L, 10);
        mockSearchResponse("/top-headlines", objectMapper.writeValueAsString(expected));
        SearchResponse actual=gNewsClient.searchTopHeadlines(Category.GENERAL, null, null, null, null, "test-apikey");
        assertEqualSearchResults(expected, actual);
    }

    @Test
    void searchTopHeadlinesEmpty() throws IOException {
        SearchResponse expected=new SearchResponse(0L, List.of());
        mockSearchResponse("/top-headlines", objectMapper.writeValueAsString(expected));
        SearchResponse actual=gNewsClient.searchTopHeadlines(Category.GENERAL, null, null, null, null, "test-apikey");
        assertEqualSearchResults(expected, actual);
    }

    private void assertEqualSearchResults(SearchResponse expected, SearchResponse actual) {
        assertEquals(expected.totalArticles(), actual.totalArticles());
        List<Article> expectedArticles=expected.articles();
        List<Article> resultArticles=actual.articles();
        assertEquals(expectedArticles.size(), resultArticles.size());
    }

    private void mockSearchResponse(String path, String jsonResponse) {
        new MockServerClient(SERVER_ADDRESS, serverPort)
                .when(request().withPath(path).withMethod(HttpMethod.GET.name()), exactly(1))
                .respond(response().withStatusCode(HttpStatus.OK.value()).withContentType(MediaType.APPLICATION_JSON)
                        .withBody(jsonResponse));

    }

    public static class PropertyOverrideContextInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext, "gnews.api.baseUrl=http://localhost:" + serverPort);
        }
    }
}
