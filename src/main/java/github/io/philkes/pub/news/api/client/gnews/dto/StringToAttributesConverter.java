package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToAttributesConverter implements Converter<String, Attributes> {

    @Override
    public Attributes convert(String source) {
        return Attributes.valueOfName(source);
    }
}
