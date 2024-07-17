package roomescape.support;

import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingRequest;
import roomescape.apply.theme.domain.Theme;

public class ReservationWaitingFixture {
    public static ReservationWaiting reservationWaiting(ReservationTime reservationTime,
                                                        Theme theme,
                                                        long memberId
    ) {
        return ReservationWaiting.of(reservationTime, theme, "2099-12-02", memberId);
    }

    public static ReservationWaiting reservationWaiting(ReservationTime reservationTime,
                                                        Theme theme,
                                                        String date,
                                                        long memberId
    ) {
        return ReservationWaiting.of(reservationTime, theme, date, memberId);
    }

    public static ReservationWaitingRequest reservationWaitingRequest(long timeId, long themeId) {
        return new ReservationWaitingRequest("2099-12-02", timeId, themeId);
    }

}
