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
}
