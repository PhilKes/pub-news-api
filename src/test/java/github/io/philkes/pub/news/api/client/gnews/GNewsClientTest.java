package github.io.philkes.pub.news.api.client.gnews;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.io.philkes.pub.news.api.TestUtils;
import github.io.philkes.pub.news.api.client.gnews.dto.Article;
import github.io.philkes.pub.news.api.client.gnews.dto.Category;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.Configuration;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
class GNewsClientTest {

    private static final String SERVER_ADDRESS="localhost";

    private static int serverPort;
    private static ClientAndServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebClient.Builder webClientBuilder;

    private GNewsClient gNewsClient;

    @BeforeEach
    void startServer() {
        serverPort=TestSocketUtils.findAvailableTcpPort();
        String serviceUrl="http://" + SERVER_ADDRESS + ":" + serverPort;

        Configuration config=Configuration.configuration().logLevel(Level.WARN);
        mockServer=startClientAndServer(config, serverPort);

        WebClient webClient=webClientBuilder
                .baseUrl(serviceUrl)
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory=HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        gNewsClient=httpServiceProxyFactory.createClient(GNewsClient.class);

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
        SearchResponse actual=gNewsClient.searchArticles("testquery", null, null, null, null, null,"test-apikey");
        assertEqualSearchResults(expected, actual);
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

}
