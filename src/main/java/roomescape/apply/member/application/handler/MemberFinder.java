package roomescape.apply.member.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.application.PasswordHasher;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.auth.ui.dto.LoginRequest;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.application.service.MemberRoleQueryService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.apply.member.ui.dto.MemberRoleNamesResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MemberFinder {

    private final PasswordHasher passwordHasher;
    private final MemberQueryService memberQueryService;
    private final MemberRoleQueryService memberRoleQueryService;

    public MemberFinder(PasswordHasher passwordHasher, MemberQueryService memberQueryService,
                        MemberRoleQueryService memberRoleQueryService) {
        this.passwordHasher = passwordHasher;
        this.memberQueryService = memberQueryService;
        this.memberRoleQueryService = memberRoleQueryService;
    }

    public List<MemberResponse> findAll() {
        return memberQueryService.findAll()
                .stream()
                .map(member -> MemberResponse.from(member.getId(), member.getName()))
                .toList();
    }

    public LoginResponse findByLoginRequest(LoginRequest request) {
        String hashPassword = passwordHasher.hashPassword(request.password());
        Member member = memberQueryService.findOneByEmailAndPassword(request.email(), hashPassword);
        Set<MemberRoleName> rolesInMember = new HashSet<>(memberRoleQueryService.findRolesInMember(member.getId()));

        return LoginResponse.from(member, MemberRoleNamesResponse.of(rolesInMember));
    }

    public LoginMember getLoginMemberByEmail(String email) {
        Member member = memberQueryService.findOneByEmail(email);
        Set<MemberRoleName> rolesInMember = new HashSet<>(memberRoleQueryService.findRolesInMember(member.getId()));
        return LoginMember.from(member, MemberRoleNamesResponse.of(rolesInMember));
    }

    public Member findOneNameById(long memberId) {
        return memberQueryService.findOneById(memberId);
    }
}
