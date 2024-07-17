package roomescape.apply.reservationwaiting.ui.dto;

import roomescape.support.checker.ReservationDateChecker;

public record ReservationWaitingRequest(
        String date,
        long timeId,
        long themeId
) {

    public ReservationWaitingRequest {
        ReservationDateChecker.validateDate(date);
    }

}
