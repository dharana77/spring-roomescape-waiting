package roomescape.domain.theme.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class ThemeJpaRepository implements ThemeRepository {

    private static final String ID = "id";

    private final EntityManager entityManager;

    public ThemeJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(Theme theme) {
        entityManager.persist(theme);
        return theme.getId();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        TypedQuery<Theme> query = entityManager.createQuery("SELECT t FROM Theme t where t.id = :id", Theme.class);
        query.setParameter(ID, id);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public List<Theme> findAll() {
        TypedQuery<Theme> query = entityManager.createQuery("SELECT t FROM Theme t", Theme.class);
        return query.getResultList();
    }

    @Override
    public void delete(Theme theme) {
        entityManager.remove(theme);
    }
}
