package roomescape.apply.reservationwaiting.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;

@Service
@Transactional(readOnly = true)
public class WaitingPositionCalculator {

    private final ReservationWaitingRepository reservationWaitingRepository;

    public WaitingPositionCalculator(ReservationWaitingRepository reservationWaitingRepository) {
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    public long calculateByReservationWaiting(ReservationWaiting waiting) {
        return reservationWaitingRepository.countByThemeIdAndDateAndTimeIdAndWaitingTimeLessThan(
                waiting.getTheme().getId(),
                waiting.getReservationDate(),
                waiting.getTime().getId(),
                waiting.getWaitingTime()
        );
    }

}
