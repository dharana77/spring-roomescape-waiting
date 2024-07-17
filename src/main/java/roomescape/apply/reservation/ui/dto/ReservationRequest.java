package roomescape.apply.reservation.ui.dto;

import roomescape.support.checker.ReservationDateChecker;

public record ReservationRequest(
        String date,
        long timeId,
        long themeId
) {

    public ReservationRequest {
        ReservationDateChecker.validateDate(date);
    }

}
