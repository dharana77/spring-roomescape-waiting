package roomescape.apply.reservation.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.ui.dto.CreateReservationResponse;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.theme.domain.Theme;

import java.util.Optional;

@Service
@Transactional
public class DuplicateReservationHandler {

    private final ReservationQueryService reservationQueryService;
    private final ReservationWaitingCommandService reservationWaitingCommandService;

    public DuplicateReservationHandler(ReservationQueryService reservationQueryService, ReservationWaitingCommandService reservationWaitingCommandService) {
        this.reservationQueryService = reservationQueryService;
        this.reservationWaitingCommandService = reservationWaitingCommandService;
    }


    public Optional<CreateReservationResponse> handleDuplicateReservation(Theme theme, String date,
                                                                          ReservationTime time, long memberId) {
        boolean notExisted = reservationQueryService.notExistsReservedReservationIn(theme.getId(), date, time.getId());
        if (notExisted) {
            return Optional.empty();
        }

        ReservationWaiting reservationWaiting = ReservationWaiting.of(time, theme, date, memberId);
        ReservationWaiting savedReservationWaiting = reservationWaitingCommandService.save(reservationWaiting);
        return Optional.of(CreateReservationResponse.createdReservationWaiting(savedReservationWaiting.getId()));
    }

}
