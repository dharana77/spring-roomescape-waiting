package roomescape.apply.reservationwaiting.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;

@Service
@Transactional
public class ReservationWaitingCommandService {

    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationWaitingCommandService(ReservationWaitingRepository reservationWaitingRepository) {
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    public void deleteById(long id) {
        reservationWaitingRepository.deleteById(id);
    }

    public ReservationWaiting save(ReservationWaiting reservationWaiting) {
        return reservationWaitingRepository.save(reservationWaiting);
    }

}
