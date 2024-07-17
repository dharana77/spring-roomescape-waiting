package roomescape.apply.member.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.ui.dto.MemberRequest;

@Service
@Transactional(readOnly = true)
public class MemberDuplicateChecker {

    private final MemberQueryService memberQueryService;

    public MemberDuplicateChecker(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    public void checkIsDuplicateEmail(MemberRequest request) {
        boolean isDuplicated = memberQueryService.isAlreadyExistEmail(request.email());

        if (isDuplicated) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
    }
}
