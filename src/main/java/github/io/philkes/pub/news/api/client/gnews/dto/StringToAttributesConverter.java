package github.io.philkes.pub.news.api.client.gnews.dto;


import org.springframework.core.convert.converter.Converter;

public class StringToAttributesConverter implements Converter<String, Attribute> {

    @Override
    public Attribute convert(String source) {
        Attribute attribute=Attribute.valueOfName(source);
        if(attribute==null) {
            throw new IllegalArgumentException("'%s' is not a valid Attribute".formatted(source));
        }
        return attribute;
    }
}
