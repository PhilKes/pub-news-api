package github.io.philkes.pub.news.api.client.gnews.dto;

public enum SortBy {
    PUBLISHED_AT("publishedAt"),
    RELEVANCE("relevance");

    private final String value;

    SortBy(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public static SortBy valueOfName(String name) {
        for(SortBy sortBy : values()) {
            if(sortBy.value.equals(name)) {
                return sortBy;
            }
        }
        return null;
    }
}
