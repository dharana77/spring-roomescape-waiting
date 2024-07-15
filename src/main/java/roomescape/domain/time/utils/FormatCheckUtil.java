package roomescape.domain.time.utils;

import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;

public class FormatCheckUtil {

    private static final String TIME_FORMAT = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";

    public static void startAtFormatCheck(String startAt) {
        if (!startAt.matches(TIME_FORMAT)) {
            throw new TimeException(TimeErrorCode.INVALID_TIME_FORMAT_ERROR);
        }
    }
}
