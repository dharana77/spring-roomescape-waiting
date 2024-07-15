package roomescape.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.utils.DateTimeCheckUtil;
import roomescape.domain.reservation.utils.FormatCheckUtil;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ReservationUtilTest {

    private static final String 현재_날짜 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String 현재_시간_5분_뒤_시간 = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String 현재_시간_5분_전_시간 = LocalTime.now().minusMinutes(10L).format(DateTimeFormatter.ofPattern("HH:mm"));

    @ParameterizedTest
    @ValueSource(strings = {"가", "a", "가나다라마바사아자차카타파하거너더러머버서"})
    void 예약_생성_중에_이름의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongNameExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.reservationNameFormatCheck(wrongNameExample)
        ).isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.INVALID_RESERVATION_NAME_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"박민욱", "브라운", "uzbeksAliddin Buriev"})
    void 예약_생성_중에_이름의_형식이_맞는_경우_예외를_발생하지_않는다(String rightNameExample) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.reservationNameFormatCheck(rightNameExample)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024.06.12", "2023-02-29", "2021-04-31", "2100-01-01", "1899-12-31"})
    void 예약_생성_중에_날짜의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongDateExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.reservationDateFormatCheck(wrongDateExample)
        ).isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.INVALID_RESERVATION_DATE_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-06-12", "2024-02-29", "1999-12-31"})
    void 예약_생성_중에_날짜의_형식이_맞는_경우_예외를_발생하지_않는다(String rightDateExample) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.reservationDateFormatCheck(rightDateExample)
        ).doesNotThrowAnyException();
    }

    @Test
    void 예약_하려는_날짜와_시간이_현재_날짜와_시간_보다_이_전이면_예외를_발생시킨다() {

        //when, then
        assertThatThrownBy(() -> DateTimeCheckUtil.isBeforeCheck(현재_날짜, 현재_시간_5분_전_시간))
                .isInstanceOf(TimeException.class)
                .hasMessage(TimeErrorCode.IS_BEFORE_ERROR.getErrorMessage());
    }

    @Test
    void 예약_하려는_날짜와_시간이_현재_날짜와_시간_보다_이_후이면_예외가_발생되지_않는다() {

        //when, then
        assertThatCode(() -> DateTimeCheckUtil.isBeforeCheck(현재_날짜, 현재_시간_5분_뒤_시간)).doesNotThrowAnyException();
    }
}
