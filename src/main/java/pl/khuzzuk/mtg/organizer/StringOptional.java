package pl.khuzzuk.mtg.organizer;

public class StringOptional {
    private String value;

    public StringOptional(String value) {
        this.value = value;
    }

    public String orElseGet(String defaultValue) {
        if (value != null && value.length() > 0) {
            return value;
        }
        return defaultValue;
    }
}
