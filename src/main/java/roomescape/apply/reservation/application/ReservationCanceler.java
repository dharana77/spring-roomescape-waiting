package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.excpetion.NotFoundReservationException;
import roomescape.apply.reservation.domain.ReservationRepository;

@Service
public class ReservationCanceler {

    private final ReservationRepository reservationRepository;

    public ReservationCanceler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void cancelReservation(long id) {
        final long reservationId = reservationRepository.findIdById(id).orElseThrow(NotFoundReservationException::new);
        reservationRepository.deleteById(reservationId);
    }
}
