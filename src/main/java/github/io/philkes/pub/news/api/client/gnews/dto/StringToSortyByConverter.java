package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToSortyByConverter implements Converter<String, SortBy> {

    @Override
    public SortBy convert(String source) {
        return SortBy.valueOfName(source);
    }
}
