package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class AdminReservationRestAssured {

    private static final String 예약_요청_시간 = "date";
    private static final String 예약시간_아이디 = "timeId";
    private static final String 테마_아이디 = "themeId";
    private static final String 사용자_아이디 = "memberId";
    private static final String 관리자_권한_토큰 = "token";

    public static ExtractableResponse<Response> 관리자_예약_생성_요청(String adminToken, String date, Long themeId, Long timeId, Long memberId, String url) {
        final Map<String, Object> reservation = getReservationForBody(date, themeId, timeId, memberId);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(관리자_권한_토큰, adminToken)
                .body(reservation)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getReservationForBody(String date, Long themeId, Long timeId, Long memberId) {
        final Map<String, Object> reservation = Map.of(
                예약_요청_시간, date,
                테마_아이디, themeId,
                예약시간_아이디, timeId,
                사용자_아이디, memberId);
        return reservation;
    }
}
