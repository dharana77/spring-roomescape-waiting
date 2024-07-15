package roomescape.acceptance.admin;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.restAssured.AdminReservationRestAssured.관리자_예약_생성_요청;
import static roomescape.restAssured.MemberRestAssured.회원가입_요청;
import static roomescape.restAssured.ThemeRestAssured.테마_생성_요청;
import static roomescape.restAssured.TimeRestAssured.예약_시간_생성_요청;

public class AdminReservationAcceptance extends AdminAcceptanceSetting {

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String 현재_시간_5분_뒤_시간 = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String ID = "id";

    private Long 테마_아이디 = null;
    private Long 예약시간_아이디 = null;
    private Long 사용자_아이디 = null;


    /**
     * given: 계정을 생성하고 관리자 권한을 부여한 뒤, 예약 생성에 필요한 테마, 예약 시간 및 사용자 생성을 요청할 수 있다.
     */
    @BeforeEach
    void 관리자_계정을_생성하고_예약_생성에_필요한_테마와_예약_시간과_사용자_생성을_요청한다() {

        //given
        super.억지로_관리자_계정을_생성한다();
        테마_아이디 = 테마_생성_요청("오싹 공포 테마", "오싹 그잡채", "https://youtube.com", "/themes").jsonPath().getLong(ID);
        예약시간_아이디 = 예약_시간_생성_요청(현재_시간_5분_뒤_시간, "/times").jsonPath().getLong(ID);
        사용자_아이디 = 회원가입_요청("uo5234@naver.com", "Password12!", "박민욱", "/members").jsonPath().getLong(ID);
    }

    /**
     * given : @BeforeEach
     * when : 관리자 권한의 API를 통해 예약 세부 정보(테마, 예약 시간, 사용자)를 선택해서 예약 요청을 하면
     * then : 200 상태코드와 예약 아이디를 검증할 수 있다.
     */
    @Test
    void 관리자가_직접_예약_세부_정보를_선택해서_예약을_등록할_수_있다() {

        //when
        ExtractableResponse<Response> response = 관리자_예약_생성_요청(관리자_토큰, CURRENT_DATE, 테마_아이디, 예약시간_아이디, 사용자_아이디, "/admin/reservations");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isNotNull();
    }
}
