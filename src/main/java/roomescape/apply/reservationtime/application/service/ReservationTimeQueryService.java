package roomescape.apply.reservationtime.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeQueryService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeQueryService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }


    public Long findIdById(long id) {
        return reservationTimeRepository.findIdById(id)
                .orElseThrow(NotFoundReservationTimeException::new);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime findOneById(long id) {
        return reservationTimeRepository.findOneById(id)
                .orElseThrow(NotFoundReservationTimeException::new);
    }

    public List<AvailableReservationTimeResponse> findAllReservedTimesByThemeIdAndDate(Long themeId,
                                                                                       String date

    ) {
        return reservationTimeRepository.findAllReservedTimesByThemeIdAndDate(themeId, date);
    }
}
