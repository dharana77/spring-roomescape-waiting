package roomescape.apply.reservationwaiting.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationwaiting.application.exception.NotFoundReservationWaitingException;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;

@Service
public class ReservationWaitingDeleter {

    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationWaitingDeleter(ReservationWaitingRepository reservationWaitingRepository) {
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    @Transactional
    public void deleteReservationWaiting(long id) {
        final long existId = reservationWaitingRepository.findIdById(id)
                                                         .orElseThrow(NotFoundReservationWaitingException::new);
        reservationWaitingRepository.deleteById(existId);
    }

}
