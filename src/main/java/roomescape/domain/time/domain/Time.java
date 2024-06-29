package roomescape.domain.time.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startAt;

    public Time(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Time() {
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
