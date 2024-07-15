package roomescape.acceptance.user;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.MemberRestAssured.로그인_요청;
import static roomescape.restAssured.MemberRestAssured.회원가입_요청;
import static roomescape.restAssured.ReservationRestAssured.*;
import static roomescape.restAssured.ThemeRestAssured.테마_생성_요청;
import static roomescape.restAssured.TimeRestAssured.예약_시간_생성_요청;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationAcceptance {

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String FIVE_MINUTES_AHEAD_OF_THE_CURRENT_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String TEN_MINUTES_AHEAD_OF_THE_CURRENT_TIME = LocalTime.now().plusMinutes(10L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String ONE_HOUR_AHEAD_OF_THE_CURRENT_TIME = LocalTime.now().plusHours(1L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String ID = "id";
    private static final String TOKEN = "token";

    private Long 공포_테마_아이디 = null;
    private Long 알쏭달쏭_테마_아이디 = null;
    private Long 추리_테마_아이디 = null;
    private Long 오_분_뒤_예약시간_아이디 = null;
    private Long 한_시간_뒤_예약시간_아이디 = null;
    private Long 십_분_뒤_예약시간_아이디 = null;
    private String 첫_번째_사용자_토큰 = null;
    private String 두_번째_사용자_토큰 = null;


    /**
     * given: 예약 생성에 필요한 테마, 예약 시간 및 사용자 생성을 요청한다.
     */
    @BeforeEach
    void 계정을_생성하고_예약_생성에_필요한_테마와_예약_시간을_요청한다() {

        //given
        회원가입_요청("uo5234@naver.com", "Password12!", "박민욱", "/members").jsonPath().getLong(ID);
        회원가입_요청("orange@naver.com", "Orange12@", "우주황", "/members").jsonPath().getLong(ID);
        첫_번째_사용자_토큰 = 로그인_요청("uo5234@naver.com", "Password12!", "/login").cookie(TOKEN);
        두_번째_사용자_토큰 = 로그인_요청("orange@naver.com", "Orange12@", "/login").cookie(TOKEN);
        공포_테마_아이디 = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes").jsonPath().getLong(ID);
        알쏭달쏭_테마_아이디 = 테마_생성_요청("알쏭 달쏭 테마", "알쏭 달쏭 그잡채", "https://youtube.com/curious", "/themes").jsonPath().getLong(ID);
        추리_테마_아이디 = 테마_생성_요청("지끈 추리 테마", "추리 그잡채", "https://youtube.com/reasoning", "/themes").jsonPath().getLong(ID);
        오_분_뒤_예약시간_아이디 = 예약_시간_생성_요청(FIVE_MINUTES_AHEAD_OF_THE_CURRENT_TIME, "/times").jsonPath().getLong(ID);
        십_분_뒤_예약시간_아이디 = 예약_시간_생성_요청(TEN_MINUTES_AHEAD_OF_THE_CURRENT_TIME, "/times").jsonPath().getLong(ID);
        한_시간_뒤_예약시간_아이디 = 예약_시간_생성_요청(ONE_HOUR_AHEAD_OF_THE_CURRENT_TIME, "/times").jsonPath().getLong(ID);
    }

    /**
     * given : @BeforeEach
     * when : 사용자는 예약 세부 정보(테마, 예약 시간)를 선택하고 예약 요청을 하면
     * then : 200 상태코드와 예약 아이디를 검증할 수 있다.
     */
    @Test
    void 사용자는_예약_세부_정보를_선택해서_예약을_등록할_수_있다() {

        //when
        ExtractableResponse<Response> response = 예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();
    }

    /**
     * given : @BeforeEach + 예약 생성 요청
     * when : 사용자는 예약 아이디를 기준으로 자신의 예약을 삭제 요청하면
     * then : 200 상태코드를 검증할 수 있다.
     */
    @Test
    void 사용자는_자신의_예약을_삭제할_수_있다() {

        //given
        final Long id = 예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 예약_삭제_요청(id, "/reservations");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given : @BeforeEach + 3개의 임의의 예약 생성 요청(나의 예약 * 2 + 상대방 예약 * 1)
     * when : 전체 예약 요청을 하면
     * then : 200 상태코드와를 전체 예약의 수를 검증할 수 있다.
     */
    @Test
    void 전체_예약을_조회할_수_있다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations").jsonPath().getLong(ID);
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 십_분_뒤_예약시간_아이디, 알쏭달쏭_테마_아이디, "/reservations").jsonPath().getLong(ID);
        예약_생성_요청(두_번째_사용자_토큰, "우주황", CURRENT_DATE, 한_시간_뒤_예약시간_아이디, 추리_테마_아이디, "/reservations").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 전체_예약_조회_요청("/reservations");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(3);
    }

    /**
     * given : @BeforeEach + 3개의 임의의 예약 생성 요청(나의 예약 * 2 + 상대방 예약 * 1)
     * when : 내 예약 요청을 하면
     * then : 200 상태코드와를 나의 예약의 수를 검증할 수 있다.
     */
    @Test
    void 나의_예약을_조회할_수_있다() {

        //given
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 오_분_뒤_예약시간_아이디, 공포_테마_아이디, "/reservations").jsonPath().getLong(ID);
        예약_생성_요청(첫_번째_사용자_토큰, "박민욱", CURRENT_DATE, 십_분_뒤_예약시간_아이디, 알쏭달쏭_테마_아이디, "/reservations").jsonPath().getLong(ID);
        예약_생성_요청(두_번째_사용자_토큰, "우주황", CURRENT_DATE, 한_시간_뒤_예약시간_아이디, 추리_테마_아이디, "/reservations").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 내_예약_조회_요청(첫_번째_사용자_토큰, "/reservations/mine");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(2);
    }
}
