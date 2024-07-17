package roomescape.apply.reservationwaiting.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReservationWaitingFinder {
    private final WaitingPositionCalculator waitingPositionCalculator;
    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationWaitingFinder(WaitingPositionCalculator waitingPositionCalculator, ReservationWaitingRepository reservationWaitingRepository) {
        this.waitingPositionCalculator = waitingPositionCalculator;
        this.reservationWaitingRepository = reservationWaitingRepository;
    }

    public List<ReservationWaitingResponse> findReservationWaitingListByMemberId(Long memberId) {
        final var reservationWaitingList = reservationWaitingRepository.findAllByMemberId(memberId);
        return reservationWaitingList.stream().map(it -> {
            long waitingPosition = waitingPositionCalculator.calculateByReservationWaiting(it);
            return ReservationWaitingResponse.from(it,
                                                   waitingPosition,
                                                   ThemeResponse.from(it.getTheme()),
                                                   ReservationTimeResponse.from(it.getTime())
            );
        }).toList();
    }

    public List<ReservationWaitingResponse> findReservationWaitingListByMemberIdOneQuery(Long memberId) {
        return reservationWaitingRepository.findAllWithPositionByMemberId(memberId);
    }

    public Optional<ReservationWaiting> findOldestReservationWaitingBy(Long themeId,
                                                                       String date,
                                                                       Long timeId) {
        return reservationWaitingRepository.findOldestWaitingByThemeIdAndDateAndTimeId(themeId, date, timeId);
    }
}

