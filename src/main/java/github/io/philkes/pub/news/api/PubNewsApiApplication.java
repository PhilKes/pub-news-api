package github.io.philkes.pub.news.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class PubNewsApiApplication {

    public static final String CACHE_ARTICLES="articles";
    public static final String CACHE_TOP_HEADLINES="topHeadlines";

    public static void main(String[] args) {
        SpringApplication.run(PubNewsApiApplication.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_ARTICLES, CACHE_TOP_HEADLINES);
    }

}
