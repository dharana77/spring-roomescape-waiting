package roomescape.apply.reservationwaiting.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingRequest;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.ui.dto.ThemeResponse;

@Service
public class ReservationWaitingSaver {

    private final ThemeQueryService themeQueryService;
    private final ReservationTimeQueryService reservationTimeQueryService;
    private final ReservationWaitingQueryService reservationWaitingQueryService;
    private final ReservationWaitingCommandService reservationWaitingCommandService;

    public ReservationWaitingSaver(ThemeQueryService themeQueryService,
                                   ReservationTimeQueryService reservationTimeQueryService,
                                   ReservationWaitingQueryService reservationWaitingQueryService,
                                   ReservationWaitingCommandService reservationWaitingCommandService
    ) {
        this.themeQueryService = themeQueryService;
        this.reservationTimeQueryService = reservationTimeQueryService;
        this.reservationWaitingQueryService = reservationWaitingQueryService;
        this.reservationWaitingCommandService = reservationWaitingCommandService;
    }

    @Transactional
    public ReservationWaitingResponse saveReservationWaitingBy(ReservationWaitingRequest request,
                                                               LoginMember loginMember) {
        final Theme theme = themeQueryService.findOneById(request.themeId());
        final ReservationTime time = reservationTimeQueryService.findOneById(request.timeId());
        final var reservationWaiting = ReservationWaiting.of(time, theme, request.date(), loginMember.id());
        final ReservationWaiting saved = reservationWaitingCommandService.save(reservationWaiting);
        final long waitingPosition = reservationWaitingQueryService.calculateByReservationWaiting(saved);

        return ReservationWaitingResponse.from(saved,
                waitingPosition,
                ThemeResponse.from(saved.getTheme()),
                ReservationTimeResponse.from(saved.getTime())
        );
    }

}
