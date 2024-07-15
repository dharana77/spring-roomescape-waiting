package roomescape.acceptance.user;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.MemberRestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberAcceptance {

    private static final String ID = "id";
    private static final String TOKEN = "token";

    /**
     * given :
     * when : 사용자는 사용자의 개인 정보(이메일, 비밀번호, 이름)를 선택하여 회원가입을 요청하면
     * then : 200 상태코드와 사용자 아이디를 검증할 수 있다.
     */
    @Test
    void 사용자는_회원가입을_할_수_있다() {

        //when
        ExtractableResponse<Response> response = 회원가입_요청("email@naver.com", "Password123!", "박민욱", "/members");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();
    }

    /**
     * given : 회원가입 * 2
     * when : 전체 회원을 조회하면
     * then : 200 상태코드와를 전체 회원의 수를 검증할 수 있다.
     */
    @Test
    void 전체_사용자_조회를_할_수_있다() {

        //given
        회원가입_요청("email@naver.com", "Password123!", "박민욱", "/members");
        회원가입_요청("admin@naver.com", "Admin123$", "관리자", "/members");

        //when
        ExtractableResponse<Response> response = 전체_사용자_조회_요청("/members");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(2);
    }

    /**
     * given : 회원가입
     * when : 사용자는 자신의 개인 정보(이메일, 비밀번호)를 선택하여 로그인을 요청하면
     * then : 200 상태코드와 토큰을 검증할 수 있다.
     */
    @Test
    void 사용자는_로그인을_할_수_있다() {

        //given
        회원가입_요청("email@naver.com", "Password123!", "박민욱", "/members");

        //when
        ExtractableResponse<Response> response = 로그인_요청("email@naver.com", "Password123!", "/login");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie(TOKEN)).isNotNull();
    }

    /**
     * given : 회원가입 + 로그인
     * when : 로그인을 통해 전달받은 토큰으로 사용자의 정보를 요청하면
     * then : 200 상태코드와 사용자 아이디를 검증할 수 있다.
     */
    @Test
    void 토큰으로_사용자_정보를_요청할_수_있다() {

        //given
        회원가입_요청("email@naver.com", "Password123!", "박민욱", "/members");
        String 토큰 = 로그인_요청("email@naver.com", "Password123!", "/login").cookie(TOKEN);

        //when
        ExtractableResponse<Response> response = 토큰으로_사용자_정보_요청(토큰, "login/check");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();
    }
}
