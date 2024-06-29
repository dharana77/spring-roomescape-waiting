package roomescape.domain.member.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class MemberJpaRepository implements MemberRepository {

    private final EntityManager entityManager;

    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        TypedQuery<Member> query = entityManager.createQuery(
                "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password", Member.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<Member> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Member member = entityManager.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    @Override
    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    @Override
    public List<Member> findAll() {
        TypedQuery<Member> query = entityManager.createQuery("SELECT m FROM Member m", Member.class);
        return query.getResultList();
    }

    @Override
    public Long updateAdminRole(Long id) {
        Member member = entityManager.find(Member.class, id);
        if (member != null) {
            member.grantAdminRole();
            entityManager.persist(member);
            return member.getId();
        }
        throw new MemberException(MemberErrorCode.INVALID_MEMBER_DETAILS_ERROR);
    }
}
