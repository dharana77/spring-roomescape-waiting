package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class ReservationRestAssured {

    private static final String 이름 = "name";

    private static final String 예약_시간 = "date";
    private static final String 시간_아이디 = "timeId";
    private static final String 테마_아이디 = "themeId";
    private static final String 토큰 = "token";
    private static final String TOKEN = "token";

    public static ExtractableResponse<Response> 예약_생성_요청(String token, String name, String currentDate, Long timeId, Long themeId, String url) {
        final Map<String, Object> reservation = getReservationForBody(name, currentDate, timeId, themeId);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(토큰, token)
                .body(reservation)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 예약_삭제_요청(Long id, String url) {
        return RestAssured.given().log().all()
                .when().delete(url + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 전체_예약_조회_요청(String url) {
        return RestAssured.given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_예약_조회_요청(String token, String url) {
        return RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getReservationForBody(String name, String currentDate, Long timeId, Long themeId) {
        return Map.of(
                이름, name,
                예약_시간, currentDate,
                시간_아이디, timeId,
                테마_아이디, themeId);
    }
}
