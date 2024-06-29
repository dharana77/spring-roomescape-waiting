package roomescape.domain.member.domain.repository;

import roomescape.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    Long save(Member member);

    List<Member> findAll();

    Long updateAdminRole(Long id);
}
