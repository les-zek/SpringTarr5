package pl.sda.spring_start.model;

public enum Category {
    IT("it"),
    DEV_OPS("dev ops"),
    DS("data science");

    private String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
