package roomescape.domain.member.api.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.MemberRequest;
import roomescape.domain.member.service.dto.MemberResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
public class ApiMemberController {

    public ApiMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> save(@RequestBody MemberRequest memberRequest) {
        MemberResponse savedMemberResponse = memberService.save(memberRequest);
        return ResponseEntity.ok().body(savedMemberResponse);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> memberResponseList = memberService.findAll();
        return ResponseEntity.ok().body(memberResponseList);
    }
}
