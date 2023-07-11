package github.io.philkes.pub.news.api.client.gnews;

import github.io.philkes.pub.news.api.client.gnews.dto.StringToAttributesConverter;
import github.io.philkes.pub.news.api.client.gnews.dto.StringToCategoryConverter;
import github.io.philkes.pub.news.api.client.gnews.dto.StringToSortByConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

/**
 * Spring Configuration to provied {@link GNewsClient} implementation
 */
@Configuration
public class GNewsClientAutoConfiguration implements WebMvcConfigurer {

    @Value("${gnews.api.baseUrl:https://gnews.io/api/v4}")
    private String apiBaseUrl;

    @Value("${gnews.api.key}")
    private String apiKey;

    @Bean
    public GNewsClient gNewsClient(WebClient.Builder webClientBuilder) {
        WebClient webClient=webClientBuilder
                .baseUrl(apiBaseUrl)
                .uriBuilderFactory(new DefaultUriBuilderFactory(){
                    @Override
                    public UriBuilder builder() {
                        return super.builder().queryParam("apikey", apiKey);
                    }
                })
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory=HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(GNewsClient.class);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSortByConverter());
        registry.addConverter(new StringToAttributesConverter());
        registry.addConverter(new StringToCategoryConverter());
    }
}
