package github.io.philkes.pub.news.api.client.gnews;

import github.io.philkes.pub.news.api.client.gnews.dto.StringToAttributesConverter;
import github.io.philkes.pub.news.api.client.gnews.dto.StringToCategoryConverter;
import github.io.philkes.pub.news.api.client.gnews.dto.StringToSortByConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Configuration to provied {@link GNewsClient} implementation
 */
@Configuration
public class GNewsClientAutoConfiguration implements WebMvcConfigurer {

    @Bean
    public GNewsClient gNewsClient(WebClient.Builder webClientBuilder,GNewsClientProperties gNewsClientProperties) {
        WebClient webClient=webClientBuilder
                .baseUrl(gNewsClientProperties.apiBaseUrl)
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
