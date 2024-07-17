package roomescape.apply.reservationwaiting.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;

@Service
public class ReservationWaitingDeleter {

    private final ReservationWaitingQueryService reservationWaitingQueryService;
    private final ReservationWaitingCommandService reservationWaitingCommandService;

    public ReservationWaitingDeleter(ReservationWaitingQueryService reservationWaitingQueryService,
                                     ReservationWaitingCommandService reservationWaitingCommandService
    ) {
        this.reservationWaitingQueryService = reservationWaitingQueryService;
        this.reservationWaitingCommandService = reservationWaitingCommandService;
    }

    @Transactional
    public void deleteReservationWaiting(long id) {
        final long existId = reservationWaitingQueryService.findIdById(id);
        reservationWaitingCommandService.deleteById(existId);
    }

}
