package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class ThemeRestAssured {

    private static final String 이름 = "name";
    private static final String 설명 = "description";
    private static final String 썸네일 = "thumbnail";

    public static ExtractableResponse<Response> 테마_생성_요청(String name, String description, String thumbnail, String url) {
        final Map<String, Object> theme = getThemeForBody(name, description, thumbnail);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 전체_테마_조회_요청(String url) {
        return RestAssured.given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테마_삭제_요청(Long id, String url) {
        return RestAssured.given().log().all()
                .when().delete(url + "/" + id)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getThemeForBody(String name, String description, String thumbnail) {
        return Map.of(
                이름, name,
                설명, description,
                썸네일, thumbnail);
    }
}
