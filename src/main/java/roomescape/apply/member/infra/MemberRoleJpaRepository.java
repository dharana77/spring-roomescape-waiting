package roomescape.apply.member.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.member.domain.CustomMemberRoleRepository;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.MemberRoleRepository;

@SuppressWarnings("unused")
public interface MemberRoleJpaRepository extends MemberRoleRepository,
                                                 JpaRepository<MemberRole, Long>,
                                                 CustomMemberRoleRepository {
}
