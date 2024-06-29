package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberRoleFinder {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleFinder(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    public Set<MemberRoleName> findRolesInMember(long memberId) {
        return memberRoleRepository.findNamesByMemberId(memberId).stream()
                .collect(Collectors.toSet());
    }

}
