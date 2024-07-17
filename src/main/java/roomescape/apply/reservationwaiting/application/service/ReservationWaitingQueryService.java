package roomescape.apply.reservationwaiting.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationwaiting.application.exception.NotFoundReservationWaitingException;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReservationWaitingQueryService {

    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationWaitingQueryService(ReservationWaitingRepository reservationWaitingRepository) {
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    public Long findIdById(long id) {
        return reservationWaitingRepository.findIdById(id)
                .orElseThrow(NotFoundReservationWaitingException::new);
    }

    public long calculateByReservationWaiting(ReservationWaiting reservationWaiting) {
        return reservationWaitingRepository.countOtherWaitingByThemeDateTimeAndWaitingTime(
                reservationWaiting.getId(),
                reservationWaiting.getTheme().getId(),
                reservationWaiting.getReservationDate(),
                reservationWaiting.getTime().getId(),
                reservationWaiting.getWaitingTime()
        );
    }

    public List<ReservationWaitingResponse> findAllWithPositionByMemberId(Long memberId) {
        return reservationWaitingRepository.findAllWithPositionByMemberId(memberId);
    }

    public Optional<ReservationWaiting> findOldestWaitingBy(Long themeId, String date, Long timeId) {
        return reservationWaitingRepository.findOldestWaitingByThemeIdAndDateAndTimeId(themeId, date, timeId);
    }
}
