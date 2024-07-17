package roomescape.apply.reservationwaiting.ui.dto;

import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.theme.ui.dto.ThemeResponse;

public record ReservationWaitingResponse(
        long id,
        String waitingPositionText,
        long waitingPosition,
        String date,
        ThemeResponse theme,
        ReservationTimeResponse time
) {
    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting,
                                                  long waitingPosition,
                                                  ThemeResponse themeResponse,
                                                  ReservationTimeResponse reservationTimeResponse
    ) {
        waitingPosition++;
        String waitingPositionText = waitingPosition + "번째 예약 대기";
        return new ReservationWaitingResponse(reservationWaiting.getId(),
                                              waitingPositionText,
                                              waitingPosition,
                                              reservationWaiting.getReservationDate(), themeResponse,
                                              reservationTimeResponse);
    }
}
