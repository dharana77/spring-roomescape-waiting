package roomescape.acceptance.user;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.ThemeRestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeAcceptance {

    private static final String ID = "id";

    /**
     * given :
     * when : 사용자는 테마 세부 내용(이름, 설명, 썸네일)을 선택하고 테마 생성 요청을 하면
     * then : 200 상태코드와 테마 아이디를 검증할 수 있다.
     */
    @Test
    void 사용자는_테마를_생성할_수_있다() {

        //when
        ExtractableResponse<Response> response = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();
    }

    /**
     * given : 테마 생성 요청 * 2
     * when : 전체 테마를 조회하면
     * then : 200 상태코드와 전체 테마 조회의 수를 검증할 수 있다.
     */
    @Test
    void 전체_테마를_조회_할_수_있다() {

        //given
        테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes");
        테마_생성_요청("알쏭 달쏭 테마", "알쏭 달쏭 그잡채", "https://youtube.com/curious", "/themes").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 전체_테마_조회_요청("/themes");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(2);
    }

    /**
     * given : 테마 생성 요청
     * when : 사용자는 테마 아이디를 기준으로 자신의 테마를 삭제 요청하면
     * then : 200 상태코드를 검증할 수 있다.
     */
    @Test
    void 사용자는_자신의_테마를_삭제할_수_있다() {

        //given
        long themeId = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 테마_삭제_요청(themeId, "/themes");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
