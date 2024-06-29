package roomescape.apply.reservationtime.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;

@SuppressWarnings("unused")
public interface ReservationTimeJpaRepository extends ReservationTimeRepository, JpaRepository<ReservationTime, Long> {

}
