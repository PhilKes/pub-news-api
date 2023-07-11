package github.io.philkes.pub.news.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.io.philkes.pub.news.api.TestUtils;
import github.io.philkes.pub.news.api.client.gnews.GNewsClient;
import github.io.philkes.pub.news.api.client.gnews.dto.SearchResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static github.io.philkes.pub.news.api.web.NewsController.HEADER_TOTAL_ELEMENTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GNewsClient gNewsClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void searchArticles200Ok() throws Exception {
        SearchResponse expected=TestUtils.createSearchResponse(100L, 10);
        Mockito.when(gNewsClient.searchArticles(anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expected);
        mockMvc.perform(get("/api/news")
                        .queryParam("q", "test-query")
                        .queryParam("from", "2022-07-11T13:43:38Z")
                        .queryParam("to", "2023-07-11T13:43:38Z")
                        .queryParam("max", "10")
                        .queryParam("sortBy", "publishedAt")
                        .queryParam("in", "title")
                        .queryParam("apiKey", "test-apikey")

                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HEADER_TOTAL_ELEMENTS, "100"))
                .andExpect(content().json(objectMapper.writeValueAsString(expected.articles())));
    }

    @Test
    void searchArticles400BadRequest() throws Exception {
        SearchResponse expected=TestUtils.createSearchResponse(100L, 10);
        Mockito.when(gNewsClient.searchArticles(anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expected);
        mockMvc.perform(get("/api/news").queryParam("apiKey", "test-apikey"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchArticles503ServiceUnavailable() throws Exception {
        Mockito.when(gNewsClient.searchArticles(anyString(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new WebClientResponseException(HttpStatus.GONE.value(), "Service gone", null, null, null));
        mockMvc.perform(get("/api/news")
                        .queryParam("q", "test-query")
                        .queryParam("apiKey", "test-apikey")
                ).andDo(print())
                .andExpect(status().is(HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

}
