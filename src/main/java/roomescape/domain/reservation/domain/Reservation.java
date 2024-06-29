package roomescape.domain.reservation.domain;

import jakarta.persistence.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;

    @OneToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @OneToOne
    @JoinColumn(name = "time_id")
    private Time time;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Reservation(Long id, String name, String date, Theme theme, Time time, Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
        this.member = member;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Theme getTheme() {
        return theme;
    }

    public Time getTime() {
        return time;
    }

    public Member getMember() {
        return member;
    }
}
