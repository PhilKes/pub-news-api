package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        return Category.valueOfName(source);
    }
}
