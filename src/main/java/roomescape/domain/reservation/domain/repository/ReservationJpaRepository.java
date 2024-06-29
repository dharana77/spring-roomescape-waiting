package roomescape.domain.reservation.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;


@Repository
@Primary
public class ReservationJpaRepository implements ReservationRepository {

    private static final String FIND_BY_ID_SQL = """
            SELECT r
            FROM Reservation r
            JOIN FETCH r.theme t
            JOIN FETCH r.time ti
            JOIN FETCH r.member m
            WHERE r.id = :id
            """;
    private static final String FIND_ALL_SQL = """
            SELECT r
            FROM Reservation r
            JOIN FETCH r.theme t
            JOIN FETCH r.time ti
            JOIN FETCH r.member m
            """;
    private static final String ID = "id";

    private final EntityManager entityManager;

    public ReservationJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        TypedQuery<Reservation> query = entityManager.createQuery(FIND_BY_ID_SQL, Reservation.class);
        query.setParameter(ID, reservationId);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public Long save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation.getId();
    }

    @Override
    public List<Reservation> findAll() {
        TypedQuery<Reservation> query = entityManager.createQuery(FIND_ALL_SQL, Reservation.class);
        return query.getResultList();
    }

    @Override
    public void delete(Reservation reservation) {
        entityManager.remove(reservation);
    }
}
