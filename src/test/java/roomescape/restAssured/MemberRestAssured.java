package roomescape.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class MemberRestAssured {

    private static final String 이메일 = "email";
    private static final String 비밀번호 = "password";
    private static final String 이름 = "name";
    private static final String 토큰 = "token";

    public static ExtractableResponse<Response> 회원가입_요청(String email, String password, String name, String url) {
        final Map<String, Object> member = getJoinMemberForBody(email, password, name);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password, String url) {
        final Map<String, Object> member = getLoginMemberForBody(email, password);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰으로_사용자_정보_요청(String token, String url) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(토큰, token)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 전체_사용자_조회_요청(String url) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 관리자_권한_부여_요청(String adminToken, Long memberId, String prefixUrl, String suffixUrl) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(토큰, adminToken)
                .when().put(prefixUrl + "/" + memberId + suffixUrl)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getJoinMemberForBody(String email, String password, String name) {
        return Map.of(
                이메일, email,
                비밀번호, password,
                이름, name);
    }

    private static Map<String, Object> getLoginMemberForBody(String email, String password) {
        return Map.of(
                이메일, email,
                비밀번호, password);
    }
}
