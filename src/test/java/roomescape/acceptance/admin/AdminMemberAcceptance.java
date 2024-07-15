package roomescape.acceptance.admin;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.MemberRestAssured.관리자_권한_부여_요청;
import static roomescape.restAssured.MemberRestAssured.회원가입_요청;


public class AdminMemberAcceptance extends AdminAcceptanceSetting {

    private static final String ID = "id";
    private static final String ROLE = "role";

    /**
     * given : AdminAcceptanceSetting - @BeforeEach + 회원가입
     * when : 관리자 권한의 API를 통해 사용자 아이디를 기준으로 관리자 권한 부여 요청하면
     * then : 200 상태코드와 사용자의 권한이 'ADMIN'을 검증을 할 수 있다.
     */
    @Test
    void 사용자에게_관리자_권한을_부여할_수_있다() {

        //given
        Long 사용자_아이디 = 회원가입_요청("email@naver.com", "Password123!", "박민욱", "/members").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 관리자_권한_부여_요청(관리자_토큰, 사용자_아이디, "/admin/members", "/role/admin");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }
}
