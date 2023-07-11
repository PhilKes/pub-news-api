package github.io.philkes.pub.news.api.client.gnews;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GNewsClientProperties {

    @Value("${gnews.api.baseUrl:https://gnews.io/api/v4}")
    public String apiBaseUrl;

    @Value("${gnews.api.key}")
    public String apiKey;

}
