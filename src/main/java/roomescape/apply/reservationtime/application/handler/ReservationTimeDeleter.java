package roomescape.apply.reservationtime.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservationtime.application.exception.ReservationTimeReferencedException;
import roomescape.apply.reservationtime.application.service.ReservationTimeCommandService;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;

@Service
public class ReservationTimeDeleter {

    private final ReservationQueryService reservationQueryService;
    private final ReservationTimeQueryService reservationTimeQueryService;
    private final ReservationTimeCommandService reservationTimeCommandService;

    public ReservationTimeDeleter(ReservationQueryService reservationQueryService,
                                  ReservationTimeQueryService reservationTimeQueryService,
                                  ReservationTimeCommandService reservationTimeCommandService
    ) {
        this.reservationQueryService = reservationQueryService;
        this.reservationTimeQueryService = reservationTimeQueryService;
        this.reservationTimeCommandService = reservationTimeCommandService;
    }


    @Transactional
    public void deleteReservationTimeBy(long id) {
        final long existId = reservationTimeQueryService.findIdById(id);

        if (reservationQueryService.isTimeIdReferenced(id)) {
            throw new ReservationTimeReferencedException();
        }

        reservationTimeCommandService.deleteById(existId);
    }
}
