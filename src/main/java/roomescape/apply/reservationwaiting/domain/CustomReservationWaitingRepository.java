package roomescape.apply.reservationwaiting.domain;

import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;

import java.util.List;

public interface CustomReservationWaitingRepository {
    List<ReservationWaitingResponse> findAllWithPositionByMemberId(Long memberId);
}
