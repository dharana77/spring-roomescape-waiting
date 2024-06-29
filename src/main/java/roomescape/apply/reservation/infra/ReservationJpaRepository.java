package roomescape.apply.reservation.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.reservation.domain.CustomReservationRepository;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;

@SuppressWarnings("unused")
public interface ReservationJpaRepository extends ReservationRepository,
                                                  JpaRepository<Reservation, Long>,
                                                  CustomReservationRepository {
}
