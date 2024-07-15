package roomescape.domain.member.error.exception;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {
    INVALID_EMAIL_OR_PASSWORD_ERROR(HttpStatus.BAD_REQUEST.value(), "아이디 혹은 비밀번호가 맞지 않습니다."),
    INVALID_MEMBER_DETAILS_ERROR(HttpStatus.BAD_REQUEST.value(), "사용자의 정보가 올바르지 않습니다."),
    NO_MEMBER_ERROR(HttpStatus.BAD_REQUEST.value(), "사용자가 존재하지 않습니다."),
    NOT_FOUND_COOKIE_ERROR(HttpStatus.BAD_REQUEST.value(), "쿠키의 정보가 올바르지 않습니다."),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED.value(), "권한이 필요합니다."),
    INVALID_MEMBER_NAME_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 이름이 입력되었습니다. 3~16자의 영, 숫자와 밑줄이 허용됩니다."),
    INVALID_MEMBER_EMAIL_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 이메일이 입력되었습니다. 일반적인 이메일 주소 형식으로 시도해주세요."),
    INVALID_MEMBER_PASSWORD_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 비밀번호가 입력되었습니다. 8~20자의 최소 1개의 소문자, 1개의 대문자, 1개의 숫자, 1개의 특수 문자가 필요합니다.");

    private final int status;
    private final String errorMessage;

    MemberErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
