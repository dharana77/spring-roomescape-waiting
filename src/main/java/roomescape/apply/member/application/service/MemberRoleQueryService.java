package roomescape.apply.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberRoleQueryService {

    private final MemberRoleRepository memberRoleRepository;

    public MemberRoleQueryService(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    public List<MemberRoleName> findRolesInMember(long memberId) {
        return memberRoleRepository.findNamesByMemberId(memberId);
    }

}
