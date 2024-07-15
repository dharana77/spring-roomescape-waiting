package roomescape.domain.member.api.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.AdminMemberResponse;

@RestController
@RequestMapping("/admin")
public class ApiAdminMemberController {

    public ApiAdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private final MemberService memberService;

    @PutMapping("/members/{memberId}/role/admin")
    public ResponseEntity<AdminMemberResponse> updateAdminRole(@PathVariable("memberId") Long id) {
        AdminMemberResponse adminMemberResponse = memberService.updateAdminRole(id);
        return ResponseEntity.ok().body(adminMemberResponse);
    }
}
