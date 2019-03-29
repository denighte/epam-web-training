package by.radchuk.task4.parser;



public enum Tag {
    DEVICES("devices"),
    DEVICE("device"),
    NAME("name"),
    DESCRIPTION("description"),
    INPUT("input"),
    CONNECTOR("connector"),
    ENERGY_USAGE("energy-usage"),
    RATING("rating"),
    PRICE("price"),
    MANUFACTURE("manufacture"),
    COMPANY("company"),
    ORIGIN("origin"),
    DATE("date");

    private String name;

    Tag(final String tagName) {
        name = tagName;
    }

    public static Tag forName(String v) {
        for (Tag tag : Tag.values()) {
            if (tag.name.equals(v)) {
                return tag;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
