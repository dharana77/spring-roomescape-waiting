package roomescape.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.theme.error.exception.ThemeErrorCode;
import roomescape.domain.theme.error.exception.ThemeException;
import roomescape.domain.theme.utils.FormatCheckUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ThemeUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "안녕", "특수_문자"})
    void 테마_생성_중에_이름의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongNameExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.themeNameFormatCheck(wrongNameExample))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.INVALID_THEME_NAME_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"HelloWorld", "안녕하세요헬로우월드입니다", "HelloWorld안녕"})
    void 테마_생성_중에_이름의_형식이_맞는_경우_예외를_발생하지_않는다(String rightNameExample) {

        //when. then
        assertThatCode(() -> FormatCheckUtil.themeNameFormatCheck(rightNameExample)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "*Hello@123*", "overTwoHundreadddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"})
    void 테마_생성_중에_설명의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongDescriptionExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.themeDescriptionCheck(wrongDescriptionExample))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.INVALID_THEME_DESCRIPTION_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"안녕하세요", "helloWorld", "HelloWorld123", "안녕하세요123", "가", "a", "twoHundreadddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"})
    void 테마_생성_중에_설명의_형식이_맞는_경우_예외를_발생하지_않는다(String rightDescriptionExample) {

        //when. then
        assertThatCode(() -> FormatCheckUtil.themeDescriptionCheck(rightDescriptionExample)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://example.com/invalid path", "ftp://example.com"})
    void 테마_생성_중에_썸네일의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongThumbnailExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.themeThumbnailFormatCheck(wrongThumbnailExample))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.INVALID_THEME_THUMBNAIL_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://example.com", "https://example.com:8080/path/to/resource", "example.com", "http://localhost:3000", "http://192.168.1.1"})
    void 테마_생성_중에_썸네일의_형식이_맞는_경우_예외를_발생하지_않는다(String rightThumbnailExample) {

        //when. then
        assertThatCode(() -> FormatCheckUtil.themeThumbnailFormatCheck(rightThumbnailExample)).doesNotThrowAnyException();
    }
}
