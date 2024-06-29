package roomescape.domain.time.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.domain.Time;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TimeJpaRepository implements TimeRepository {

    private static final String FIND_BY_THEME_ID_AND_DATE_SQL = """
            SELECT t FROM Time t
            WHERE t.id NOT IN (
                SELECT r.time.id
                FROM Reservation r
                WHERE r.theme.id = :themeId
                AND r.date = :date)
            """;
    private static final String THEME_ID = "themeId";
    private static final String DATE = "date";
    private static final String ID = "id";

    private final EntityManager entityManager;

    public TimeJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(Time time) {
        entityManager.persist(time);
        return time.getId();
    }

    @Override
    public Optional<Time> findById(Long timeId) {
        TypedQuery<Time> query = entityManager.createQuery("SELECT t FROM Time t where t.id = :id", Time.class);
        query.setParameter(ID, timeId);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public List<Time> findAll() {
        TypedQuery<Time> query = entityManager.createQuery("SELECT t FROM Time t", Time.class);
        return query.getResultList();
    }

    @Override
    public void delete(Time time) {
        entityManager.remove(time);
    }

    @Override
    public List<Time> findByThemeIdAndDate(String themeId, String date) {
        TypedQuery<Time> query = entityManager.createQuery(FIND_BY_THEME_ID_AND_DATE_SQL, Time.class);
        query.setParameter(THEME_ID, themeId);
        query.setParameter(DATE, date);
        return query.getResultList();
    }
}
