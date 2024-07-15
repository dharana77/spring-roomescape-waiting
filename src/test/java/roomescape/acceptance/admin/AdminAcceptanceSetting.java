package roomescape.acceptance.admin;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.member.service.MemberService;

import static roomescape.restAssured.MemberRestAssured.로그인_요청;
import static roomescape.restAssured.MemberRestAssured.회원가입_요청;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminAcceptanceSetting {

    private static final String ID = "id";
    private static final String TOKEN = "token";

    @Autowired
    private MemberService memberService;

    protected String 관리자_토큰 = null;

    @BeforeEach
    void 억지로_관리자_계정을_생성한다() {

        //given
        long memberId = 회원가입_요청("admin@naver.com", "Admin12!", "관리자", "/members").jsonPath().getLong(ID);
        String adminToken = 로그인_요청("admin@naver.com", "Admin12!", "/login").cookie(TOKEN);
        memberService.updateAdminRole(memberId);
        관리자_토큰 = adminToken;
    }
}
