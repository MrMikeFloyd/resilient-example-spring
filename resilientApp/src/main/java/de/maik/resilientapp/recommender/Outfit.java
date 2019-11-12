package de.maik.resilientapp.recommender;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Outfit {
    SUPER_COLD("Polar expedition outfit"),
    COLD("Winter coat"),
    INTERMEDIATE("Light jacket"),
    WARM("Sweater and long pants"),
    VERY_WARM("T-Shirt and shorts"),
    SUPER_WARM("Swimsuit/trunks"),
    UNKNOWN("Be prepared for anything");

    String value;

    Outfit(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
