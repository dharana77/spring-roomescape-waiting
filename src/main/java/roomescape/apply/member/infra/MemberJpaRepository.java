package roomescape.apply.member.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<Member, Long> {
}
