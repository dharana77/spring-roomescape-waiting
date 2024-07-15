package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class TimeRestAssured {

    private static final String START_AT = "startAt";

    public static ExtractableResponse<Response> 예약_시간_생성_요청(String startTime, String url) {
        final Map<String, Object> time = getStartAtForBody(startTime);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 전체_예약_시간_조회_요청(String url) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 예약_시간_삭제_요청(Long id, String url) {
        return RestAssured.given().log().all()
                .when().delete(url + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 특정_테마와_날짜에_맞는_예약_가능_시간_조회_요청(String date, Long themeId, String url) {
        return RestAssured.given().log().all()
                .when().get(url + "?date=" + date + "&themeId=" + themeId)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getStartAtForBody(String startTime) {
        return Map.of(START_AT, startTime);
    }
}
