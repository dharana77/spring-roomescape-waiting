package roomescape.apply.reservationtime.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.support.checker.ReservationTimeRequestChecker;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeFinder {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeFinder(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .sorted(ReservationTime::compareByStartTime)
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime findOneById(long timeId) {
        return reservationTimeRepository.findOneById(timeId).orElseThrow(NotFoundReservationTimeException::new);
    }

    public List<AvailableReservationTimeResponse> findAvailableTimesBy(String date, String themeId) {
        ReservationTimeRequestChecker.validateRequestParam(date, themeId);
        return reservationTimeRepository.findReservationTimesWithIsReservedInDateAndThemeId(date, Long.parseLong(themeId));
    }
}
