package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.excpetion.NotFoundReservationException;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservationwaiting.application.ReservationWaitingFinder;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;

import java.util.Optional;

@Service
public class ReservationCanceler {

    private final ReservationRepository reservationRepository;
    private final ReservationWaitingFinder reservationWaitingFinder;
    private final ReservationRecorder reservationRecorder;

    public ReservationCanceler(ReservationRepository reservationRepository,
                               ReservationWaitingFinder reservationWaitingFinder, ReservationRecorder reservationRecorder
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationWaitingFinder = reservationWaitingFinder;
        this.reservationRecorder = reservationRecorder;
    }

    @Transactional
    public void cancelReservation(long id) {
        Reservation foundReservation = reservationRepository.findOneById(id).orElseThrow(NotFoundReservationException::new);
        foundReservation.cancel();
        reservationRepository.save(foundReservation);
        Optional<ReservationWaiting> oldestWaiting = reservationWaitingFinder.findOldestReservationWaitingBy(
                                                        foundReservation.getTheme().getId(),
                                                        foundReservation.getReservationDate().value(),
                                                        foundReservation.getTheme().getId());
        oldestWaiting.ifPresent(reservationRecorder::recordReservationBy);
    }
}
