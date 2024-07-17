package roomescape.apply.theme.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.theme.application.exception.ThemeReferencedException;
import roomescape.apply.theme.application.service.ThemeCommandService;
import roomescape.apply.theme.application.service.ThemeQueryService;

@Service
public class ThemeDeleter {

    private final ThemeQueryService themeQueryService;
    private final ThemeCommandService themeCommandService;
    private final ReservationQueryService reservationQueryService;

    public ThemeDeleter(ThemeQueryService themeQueryService,
                        ThemeCommandService themeCommandService,
                        ReservationQueryService reservationQueryService
    ) {
        this.themeQueryService = themeQueryService;
        this.themeCommandService = themeCommandService;
        this.reservationQueryService = reservationQueryService;
    }

    @Transactional
    public void deleteThemeBy(long id) {
        final long existId = themeQueryService.findIdById(id);
        if (reservationQueryService.isThemeIdReferenced(existId)) {
            throw new ThemeReferencedException();
        }

        themeCommandService.deleteById(existId);
    }
}
