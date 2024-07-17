package roomescape.apply.reservationwaiting.domain;

import jakarta.persistence.*;
import roomescape.apply.reservation.domain.ReservationDate;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.domain.Theme;

import java.time.LocalDateTime;

@Entity
public class ReservationWaiting {

    @Id
    @Column(name = "reservation_waiting_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime waitingTime;

    @Embedded
    private ReservationDate reservationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_id",
            foreignKey = @ForeignKey(name = "fk_reservation_waiting_to_reservation_time"))
    private ReservationTime time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_id",
            foreignKey = @ForeignKey(name = "fk_reservation_waiting_to_theme"))
    private Theme theme;
    private Long memberId;

    protected ReservationWaiting() {
    }

    public static ReservationWaiting of(ReservationTime reservationTime, Theme theme, String date, long memberId) {
        ReservationWaiting reservationWaiting = new ReservationWaiting();
        reservationWaiting.waitingTime = LocalDateTime.now();
        reservationWaiting.time = reservationTime;
        reservationWaiting.theme = theme;
        reservationWaiting.reservationDate = new ReservationDate(date);
        reservationWaiting.memberId = memberId;
        return reservationWaiting;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getWaitingTime() {
        return waitingTime;
    }

    public String getReservationDate() {
        return reservationDate.value();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getMemberId() {
        return memberId;
    }

}
