package roomescape.apply.reservation.domain;

import jakarta.persistence.Embeddable;
import org.springframework.util.StringUtils;
import roomescape.support.checker.ReservationDateChecker;
import roomescape.support.domain.ValueObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
public class ReservationDate extends ValueObject<ReservationDate> {

    private String date;

    protected ReservationDate() {

    }

    public ReservationDate(String date) {
        if (!StringUtils.hasText(date)) {
            throw new IllegalArgumentException("날짜는 비어있을 수 없습니다");
        }
        ReservationDateChecker.validateDateFormat(date);
        this.date = date;
    }

    public boolean isNotBefore(String targetDate) {
        LocalDate target = LocalDate.parse(targetDate, DateTimeFormatter.ISO_DATE);
        LocalDate localDate = LocalDate.parse(this.date, DateTimeFormatter.ISO_DATE);
        return !localDate.isBefore(target);
    }

    public boolean isNotAfter(String targetDate) {
        LocalDate target = LocalDate.parse(targetDate, DateTimeFormatter.ISO_DATE);
        LocalDate localDate = LocalDate.parse(this.date, DateTimeFormatter.ISO_DATE);
        return !localDate.isAfter(target);
    }

    public String value() {
        return date;
    }

}
