package roomescape.apply.reservation.domain;

import jakarta.persistence.*;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.domain.Theme;

@Entity
public class Reservation {

    @Id @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Embedded
    private ReservationDate reservationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_id",
                foreignKey = @ForeignKey(name = "fk_reservation_to_reservation_time"))
    private ReservationTime time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_id",
                foreignKey = @ForeignKey(name = "fk_reservation_to_theme"))
    private Theme theme;
    private Long memberId;

    protected Reservation() {

    }

    public Reservation(Long id, String name, String date, ReservationTime time, Theme theme, Long memberId) {
        this.id = id;
        this.name = name;
        this.reservationDate = new ReservationDate(date);
        this.time = time;
        this.theme = theme;
        this.memberId = memberId;
    }

    public static Reservation of(String name, String date, ReservationTime time, Theme theme, Long memberId) {
        Reservation reservation = new Reservation();
        reservation.name = name;
        reservation.reservationDate = new ReservationDate(date);
        reservation.time = time;
        reservation.theme = theme;
        reservation.memberId = memberId;
        return reservation;
    }

    public void changeId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getMemberId() {
        return memberId.longValue();
    }
}
