package roomescape.acceptance.user;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.ThemeRestAssured.테마_생성_요청;
import static roomescape.restAssured.TimeRestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeAcceptance {

    private static final String ID = "id";
    private static final String 오늘_날짜 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String 현재_시간_5분_뒤_시간 = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String 현재_시간_10분_뒤_시간 = LocalTime.now().plusMinutes(10L).format(DateTimeFormatter.ofPattern("HH:mm"));


    /**
     * given :
     * when : 사용자는 예약 시간 세부 내용(시작 시간)을 선택하고 예약 시간 생성 요청을 하면
     * then : 200 상태코드와 예약 시간 아이디를 검증할 수 있다.
     */
    @Test
    void 사용자는_예약_시간을_생성할_수_있다() {

        //when
        ExtractableResponse<Response> response = 예약_시간_생성_요청(현재_시간_5분_뒤_시간, "/times");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();

    }

    /**
     * given : 예약 시간 생성 요청  * 2
     * when : 전체 예약 시간 조회 요청을 하면
     * then : 200 상태코드와를 전체 예약 시간의 수를 검증할 수 있다.
     */
    @Test
    void 전체_예약을_조회할_수_있다() {

        //given
        예약_시간_생성_요청(현재_시간_5분_뒤_시간, "/times");
        예약_시간_생성_요청(현재_시간_10분_뒤_시간, "/times");

        //when
        ExtractableResponse<Response> response = 전체_예약_시간_조회_요청("/times");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(2);
    }

    /**
     * given : 예약 시간 생성 요청
     * when : 사용자는 예약 시간 아이디를 기준으로 자신의 예약_시간 삭제 요청하면
     * then : 200 상태코드를 검증할 수 있다.
     */
    @Test
    void 사용자는_자신의_예약_시간을_삭제할_수_있다() {

        //given
        Long timeId = 예약_시간_생성_요청(현재_시간_5분_뒤_시간, "/times").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 예약_시간_삭제_요청(timeId, "/times");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given : 예약 시간 생성 요청
     * when : 사용자는 예약 시간 아이디를 기준으로 자신의 예약_시간 삭제 요청하면
     * then : 200 상태코드를 검증할 수 있다.
     */
    @Test
    void 특정_테마와_날짜에_맞는_예약_가능_시간을_조회할_수_있다() {

        //given
        예약_시간_생성_요청(현재_시간_5분_뒤_시간, "/times").jsonPath().getLong(ID);
        Long 공포_테마_아이디 = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com/fear", "/themes").jsonPath().getLong(ID);

        //when
        ExtractableResponse<Response> response = 특정_테마와_날짜에_맞는_예약_가능_시간_조회_요청(오늘_날짜, 공포_테마_아이디, "/times/available");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(1);
    }
}
