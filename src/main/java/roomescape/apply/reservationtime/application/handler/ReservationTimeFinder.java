package roomescape.apply.reservationtime.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.support.checker.ReservationTimeRequestChecker;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeFinder {

    private final ReservationTimeQueryService reservationTimeQueryService;

    public ReservationTimeFinder(ReservationTimeQueryService reservationTimeQueryService) {
        this.reservationTimeQueryService = reservationTimeQueryService;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeQueryService.findAll()
                .stream()
                .sorted(ReservationTime::compareByStartTime)
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime findOneById(long timeId) {
        return reservationTimeQueryService.findOneById(timeId);
    }

    public List<AvailableReservationTimeResponse> findAvailableTimesBy(String date, String themeId) {
        ReservationTimeRequestChecker.validateRequestParam(date, themeId);
        return reservationTimeQueryService.findAllReservedTimesByThemeIdAndDate(Long.parseLong(themeId), date);
    }
}
