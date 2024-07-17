package roomescape.apply.reservationtime.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.application.service.ReservationTimeCommandService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;

@Service
public class ReservationTimeSaver {

    private final ReservationTimeCommandService reservationTimeCommandService;

    public ReservationTimeSaver(ReservationTimeCommandService reservationTimeCommandService) {
        this.reservationTimeCommandService = reservationTimeCommandService;
    }

    @Transactional
    public ReservationTimeResponse saveReservationTimeBy(ReservationTimeRequest request) {
        final ReservationTime reservationTime = ReservationTime.of(request.startAt());
        final ReservationTime saved = reservationTimeCommandService.save(reservationTime);
        return ReservationTimeResponse.from(saved);
    }

}
