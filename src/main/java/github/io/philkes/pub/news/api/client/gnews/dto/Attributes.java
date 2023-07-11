package github.io.philkes.pub.news.api.client.gnews.dto;

public enum Attributes {
    TITLE("title"),
    DESCRIPTION("description");
    private final String name;

    Attributes(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static Attributes valueOfName(String name) {
        for(Attributes attribute : values()) {
            if(attribute.name.equals(name)) {
                return attribute;
            }
        }
        return null;
    }
}
