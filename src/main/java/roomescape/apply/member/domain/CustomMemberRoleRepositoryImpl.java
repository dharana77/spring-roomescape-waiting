package roomescape.apply.member.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Iterator;

public class CustomMemberRoleRepositoryImpl implements CustomMemberRoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveAll(Iterator<MemberRole> memberRoles) {
        memberRoles.forEachRemaining(it -> {
            if (it.getId() == null) {
                entityManager.persist(it);
            } else {
                entityManager.merge(it);
            }
        });
    }
}
