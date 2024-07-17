package roomescape.apply.reservationwaiting.ui;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.BaseWebApplicationTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


class ReservationWaitingControllerTest extends BaseWebApplicationTest {

    @Test
    @DisplayName("내 예약과 예약 대기 목록을 가져올 수 있다.")
    void getMineReservations() {
        String masterToken = getMasterToken();
        String themeId = saveAndGetThemeId(masterToken);
        String date = "2099-12-02";
        String user1Token = loginAndGetToken("테스터1", "testing1@gmail.com");
        String user2Token = loginAndGetToken("테스터2", "testing2@gmail.com");

        for (String time : List.of("10:00", "12:00", "14:00", "16:00")) {
            String timeId = saveAndGetTimeId(time, user1Token);
            if (time.equals("10:00") || time.equals("12:00")) {
                saveReservationByTimeIdAndThemeIdAndDate(date, timeId, themeId, masterToken);
                saveReservationWaitingByTimeIdAndThemeIdAndDate(date, timeId, themeId, user2Token);
                saveReservationWaitingByTimeIdAndThemeIdAndDate(date, timeId, themeId, user1Token);
                continue;
            }
            saveReservationByTimeIdAndThemeIdAndDate(date, timeId, themeId, user1Token);
        }

        String path = "/reservations/mine";

        Response response = RestAssured.given().log().all()
                .when()
                .cookie("token", user1Token)
                .get(path)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4))
                .extract().response();
        List<Map<String, Object>> expectedData = List.of(
                Map.of("name", "테스터1", "status", "예약"),
                Map.of("name", "테스터1", "status", "예약"),
                Map.of("name", "예약 대기", "status", "2번째 예약 대기"),
                Map.of("name", "예약 대기", "status", "2번째 예약 대기")
        );

        for (int i = 0; i < expectedData.size(); i++) {
            response.then()
                    .body("[" + i + "].name", is(expectedData.get(i).get("name")))
                    .body("[" + i + "].status", is(expectedData.get(i).get("status")));
        }
    }

    private void saveMember(String name, String email) {
        String token = getMasterToken();

        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("name", name);
        memberParams.put("email", email);
        memberParams.put("password", "testPassword");
        memberParams.put("roleNames", List.of("어드민"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberParams)
                .when()
                .cookie("token", token)
                .post("/members")
                .then().log().all()
                .statusCode(201)
                .body("name", is(name));
    }


    private String saveAndGetTimeId(String time, String token) {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", time);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .cookie("token", token)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().body().path("id").toString();
    }

    private String saveAndGetThemeId(String token) {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "레벨2 탈출");
        themeParams.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        themeParams.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .cookie("token", token)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().body().path("id").toString();
    }

    private void saveReservationByTimeIdAndThemeIdAndDate(String date, String timeId, String themeId, String token) {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", date);
        reservationParams.put("timeId", timeId);
        reservationParams.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .cookie("token", token)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private String loginAndGetToken(String name, String email) {
        saveMember(name, email);

        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("email", email);
        loginParam.put("password", "testPassword");
        Response loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue())
                .extract().response();

        return loginResponse.getCookie("token");
    }

    private String getMasterToken() {
        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("email", "master@gmail.com");
        loginParam.put("password", "123");
        Response loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue())
                .extract().response();

        return loginResponse.getCookie("token");
    }

    private void saveReservationWaitingByTimeIdAndThemeIdAndDate(String date, String timeId, String themeId, String token) {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("date", date);
        reservationParams.put("timeId", timeId);
        reservationParams.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .cookie("token", token)
                .when().post("/reservation-waiting")
                .then().log().all()
                .statusCode(201);
    }
}
