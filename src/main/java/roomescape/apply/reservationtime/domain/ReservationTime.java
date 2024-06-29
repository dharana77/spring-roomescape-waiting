package roomescape.apply.reservationtime.domain;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ReservationTime {

    @Id @Column(name = "reservation_time_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String startAt;

    @Transient
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    protected ReservationTime() {

    }

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(String startAt) {
        ReservationTime reservation = new ReservationTime();
        reservation.startAt = startAt;
        return reservation;
    }

    public int compareByStartTime(ReservationTime target) {
        LocalTime time1 = LocalTime.parse(this.startAt, formatter);
        LocalTime time2 = LocalTime.parse(target.getStartAt(), formatter);
        return time1.compareTo(time2);
    }

    public void changeId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
