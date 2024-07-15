package roomescape.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.utils.FormatCheckUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MemberUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"우리", "user@name"})
    void 이름_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongName) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.memberNameFormatCheck(wrongName)
        ).isInstanceOf(MemberException.class)
                .hasMessage(MemberErrorCode.INVALID_MEMBER_NAME_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"박민욱", "user123"})
    void 이름_형식이_맞는_경우_예외가_발생하지_않는다(String rightName) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.memberNameFormatCheck(rightName)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"example@.com", "user.name+tag+sorting@example.com"})
    void 이메일_형식이_맞지_않는_경우_예외를_발생시킨다(String rightEmail) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.memberEmailFormatCheck(rightEmail)
        ).isInstanceOf(MemberException.class)
                .hasMessage(MemberErrorCode.INVALID_MEMBER_EMAIL_FORMAT_ERROR.getErrorMessage());

    }

    @ParameterizedTest
    @ValueSource(strings = {"example@example.com", "uo5234@naver.com"})
    void 이메일_형식이_맞는_경우_예외를_발생하지_않는다(String rightEmail) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.memberEmailFormatCheck(rightEmail)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "Password1"})
    void 비밀번호_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongPassword) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.memberPasswordFormatCheck(wrongPassword)
        ).isInstanceOf(MemberException.class)
                .hasMessage(MemberErrorCode.INVALID_MEMBER_PASSWORD_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Str0ng@Pass"})
    void 비밀번호_형식이_맞는_경우_예외를_발생하지_않는다(String rightPassword) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.memberPasswordFormatCheck(rightPassword)).doesNotThrowAnyException();
    }
}
