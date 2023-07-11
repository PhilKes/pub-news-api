package github.io.philkes.pub.news.api.client.gnews.dto;

public enum Category {
    GENERAL("general"),
    WORLD("world"),
    NATION("nation"),
    BUSINESS("business"),
    TECHNOLOGY("technology"),
    ENTERTAINMENT("entertainment"),
    SPORTS("sports"),
    SCIENCE("science"),
    HEALTH("health");
    private final String value;

    Category(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public static Category valueOfName(String name) {
        for(Category category : values()) {
            if(category.value.equals(name)) {
                return category;
            }
        }
        return null;
    }
}
