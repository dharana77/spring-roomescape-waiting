package roomescape.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;
import roomescape.domain.time.utils.FormatCheckUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TimeUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"24:01", "-1:32", "12;30", "01:61"})
    void 시간_형식이_맞지_않는_경우_예외를_발생시킨다(String startAt) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.startAtFormatCheck(startAt)).isInstanceOf(TimeException.class).hasMessage(TimeErrorCode.INVALID_TIME_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00:00", "12:00", "23:59"})
    void 시간_형식이_맞는_경우_예외가_발생하지_않는다(String startAt) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.startAtFormatCheck(startAt)).doesNotThrowAnyException();
    }
}
