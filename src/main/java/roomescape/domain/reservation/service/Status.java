package roomescape.domain.reservation.service;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Status {

    RESERVED("reserved", "예약");

    private String name;
    private String description;

    Status(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonValue
    public String toJson() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Status findByDescription(String description) {
        return Arrays.stream(values())
                .filter(status -> status.getDescription().equals(description))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
