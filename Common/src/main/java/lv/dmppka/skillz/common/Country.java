package lv.dmppka.skillz.common;

public enum Country {
    SPAIN("Spain"), US("United States");

    private final String name;

    Country(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }
}
