package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToSortByConverter implements Converter<String, SortBy> {

    @Override
    public SortBy convert(String source) {
        SortBy sortBy=SortBy.valueOfName(source);
        if(sortBy==null) {
            throw new IllegalArgumentException("'%s' is not a valid SortBy".formatted(source));
        }
        return sortBy;
    }
}
