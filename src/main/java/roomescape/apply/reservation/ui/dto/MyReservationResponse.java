package roomescape.apply.reservation.ui.dto;

import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.ui.dto.ThemeResponse;

public record MyReservationResponse(
        long id,
        String name,
        String date,
        ThemeResponse theme,
        ReservationTimeResponse time,
        String status
) {
    public static MyReservationResponse from(Reservation reservation, Theme theme, ReservationTime reservationTime) {
        return new MyReservationResponse(reservation.getId(), reservation.getName(), reservation.getReservationDate().value(),
                ThemeResponse.from(theme), ReservationTimeResponse.from(reservationTime), reservation.getReservationStatus().toMessage());
    }

    public static MyReservationResponse fromWaitingResponse(ReservationWaitingResponse waitingResponse) {
        return new MyReservationResponse(waitingResponse.id(), "예약 대기", waitingResponse.date(),
                waitingResponse.theme(), waitingResponse.time(), waitingResponse.waitingPositionText());
    }
}
