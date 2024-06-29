package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.MemberRoleRepository;

import java.util.Set;

@Service
public class MemberRoleSaver {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleSaver(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    @Transactional
    public void saveAll(Set<MemberRole> memberRoles) {
        memberRoleRepository.saveAll(memberRoles.iterator());
    }

}
