package roomescape.apply.reservationwaiting.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingRequest;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.ui.dto.ThemeResponse;

@Service
public class ReservationWaitingSaver {
    private final ThemeFinder themeFinder;
    private final ReservationTimeFinder reservationTimeFinder;
    private final WaitingPositionCalculator waitingPositionCalculator;
    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationWaitingSaver(ThemeFinder themeFinder,
                                   ReservationTimeFinder reservationTimeFinder,
                                   WaitingPositionCalculator waitingPositionCalculator,
                                   ReservationWaitingRepository reservationWaitingRepository) {
        this.themeFinder = themeFinder;
        this.reservationTimeFinder = reservationTimeFinder;
        this.waitingPositionCalculator = waitingPositionCalculator;
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    @Transactional
    public ReservationWaitingResponse saveReservationWaitingBy(ReservationWaitingRequest request,
                                                               LoginMember loginMember) {
        final Theme theme = themeFinder.findOneById(request.themeId());
        final ReservationTime time = reservationTimeFinder.findOneById(request.timeId());
        final var reservationWaiting = ReservationWaiting.of(time, theme, request.date(), loginMember.id());
        final ReservationWaiting saved = reservationWaitingRepository.save(reservationWaiting);
        final long waitingPosition = waitingPositionCalculator.calculateByReservationWaiting(saved);

        return ReservationWaitingResponse.from(saved,
                waitingPosition,
                ThemeResponse.from(saved.getTheme()),
                ReservationTimeResponse.from(saved.getTime())
        );
    }

}
