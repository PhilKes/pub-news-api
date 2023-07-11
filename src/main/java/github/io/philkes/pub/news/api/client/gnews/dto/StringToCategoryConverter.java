package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        Category category=Category.valueOfName(source);
        if(category==null) {
            throw new IllegalArgumentException("'%s' is not a valid Category".formatted(source));
        }
        return category;
    }
}
