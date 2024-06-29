package roomescape.apply.reservationtime.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.ReservationFinder;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.application.exception.ReservationTimeReferencedException;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;

@Service
public class ReservationTimeDeleter {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationFinder reservationFinder;

    public ReservationTimeDeleter(ReservationTimeRepository reservationTimeRepository, ReservationFinder reservationFinder) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationFinder = reservationFinder;
    }

    @Transactional
    public void deleteReservationTimeBy(long id) {
        final long existId = reservationTimeRepository.findIdById(id).orElseThrow(NotFoundReservationTimeException::new);
        boolean isReferenced = reservationFinder.findIdByTimeId(id).isPresent();
        if (isReferenced) {
            throw new ReservationTimeReferencedException();
        }

        reservationTimeRepository.deleteById(existId);
    }
}
