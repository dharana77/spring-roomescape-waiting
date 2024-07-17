package roomescape.apply.member.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.application.PasswordHasher;
import roomescape.apply.member.application.service.MemberCommandService;
import roomescape.apply.member.application.service.MemberRoleCommandService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.ui.dto.MemberRequest;
import roomescape.apply.member.ui.dto.MemberResponse;

import java.util.stream.Collectors;

@Service
public class MemberAdder {

    private final PasswordHasher passwordHasher;
    private final MemberDuplicateChecker duplicateChecker;
    private final MemberCommandService memberCommandService;
    private final MemberRoleCommandService memberRoleCommandService;

    public MemberAdder(PasswordHasher passwordHasher, MemberDuplicateChecker duplicateChecker,
                       MemberCommandService memberCommandService, MemberRoleCommandService memberRoleCommandService) {
        this.passwordHasher = passwordHasher;
        this.duplicateChecker = duplicateChecker;
        this.memberCommandService = memberCommandService;
        this.memberRoleCommandService = memberRoleCommandService;
    }

    @Transactional
    public MemberResponse addNewMember(MemberRequest request) {
        duplicateChecker.checkIsDuplicateEmail(request);

        String hashPassword = passwordHasher.hashPassword(request.password());
        Member saved = memberCommandService.save(Member.of(request.name(), request.email(), hashPassword));

        var memberRoles = request.roleNamesWithDefaultValue()
                .stream()
                .map(it -> MemberRole.of(it, saved.getId()))
                .collect(Collectors.toSet());
        memberRoleCommandService.saveAll(memberRoles.iterator());

        return MemberResponse.from(saved.getId(), saved.getName());
    }

}
