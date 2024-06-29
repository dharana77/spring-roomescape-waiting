package roomescape.apply.member.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Member> findAll();

    @Query("SELECT m.id FROM Member m WHERE m.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    Member save(Member member);

    Optional<Member> findOneByEmail(String email);

    Optional<Member> findOneById(long memberId);
}
