package github.io.philkes.pub.news.api.client.gnews.dto;

public enum Attribute {
    TITLE("title"),
    DESCRIPTION("description");
    private final String name;

    Attribute(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static Attribute valueOfName(String name) {
        for(Attribute attribute : values()) {
            if(attribute.name.equals(name)) {
                return attribute;
            }
        }
        return null;
    }
}
