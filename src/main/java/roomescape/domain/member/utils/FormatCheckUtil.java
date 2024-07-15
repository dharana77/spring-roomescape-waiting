package roomescape.domain.member.utils;

import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;

public class FormatCheckUtil {

    private static final String MEMBER_NAME_FORMAT = "^[a-zA-Z0-9가-힣_]{3,16}$";
    private static final String MEMBER_EMAIL_FORMAT = "^[a-zA-Z0-9-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String MEMBER_PASSWORD_FORMAT = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

    public static void memberNameFormatCheck(String name) {
        if (!name.matches(MEMBER_NAME_FORMAT)) {
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_NAME_FORMAT_ERROR);
        }
    }

    public static void memberEmailFormatCheck(String email) {
        if (!email.matches(MEMBER_EMAIL_FORMAT)) {
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_EMAIL_FORMAT_ERROR);
        }
    }

    public static void memberPasswordFormatCheck(String password) {
        if (!password.matches(MEMBER_PASSWORD_FORMAT)) {
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_PASSWORD_ERROR);
        }
    }
}
