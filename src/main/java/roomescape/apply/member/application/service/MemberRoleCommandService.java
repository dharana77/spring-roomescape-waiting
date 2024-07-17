package roomescape.apply.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.MemberRoleRepository;

import java.util.Iterator;

@Service
@Transactional
public class MemberRoleCommandService {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleCommandService(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    public void saveAll(Iterator<MemberRole> memberRoles) {
        memberRoleRepository.saveAll(memberRoles);
    }

}
