package roomescape.apply.reservationwaiting.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.reservationwaiting.domain.CustomReservationWaitingRepository;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;

@SuppressWarnings("unused")
public interface ReservationWaitingJpaRepository extends ReservationWaitingRepository,
                                                         JpaRepository<ReservationWaiting, Long>,
                                                         CustomReservationWaitingRepository
{
}
