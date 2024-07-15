package roomescape.domain.reservation.utils;

import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.reservation.error.exception.ReservationException;

public class FormatCheckUtil {

    private static final String RESERVATION_NAME_FORMAT = "^[A-Za-z가-힣\\s]{2,20}$";
    private static final String RESERVATION_DATE_FORMAT = "^(?:(?:(?:(?:19|20)[0-9]{2})-(?:(?:0[13578]|1[02])-(?:0[1-9]|[12][0-9]|3[01])|(?:0[469]|11)-(?:0[1-9]|[12][0-9]|30)|02-(?:0[1-9]|1[0-9]|2[0-8])))|(?:((?:19|20)(?:[02468][048]|[13579][26]))-02-29))$";

    public static void reservationNameFormatCheck(String name) {
        if (!name.matches(RESERVATION_NAME_FORMAT)) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_NAME_FORMAT_ERROR);
        }
    }

    public static void reservationDateFormatCheck(String date) {
        if (!date.matches(RESERVATION_DATE_FORMAT)) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_DATE_FORMAT_ERROR);
        }
    }
}
