package roomescape.apply.reservation.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.ui.dto.MyReservationResponse;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationFinder {

    private final ReservationQueryService reservationQueryService;
    private final ReservationWaitingQueryService reservationWaitingQueryService;

    public ReservationFinder(ReservationQueryService reservationQueryService,
                             ReservationWaitingQueryService reservationWaitingQueryService
    ) {
        this.reservationQueryService = reservationQueryService;
        this.reservationWaitingQueryService = reservationWaitingQueryService;
    }

    public List<ReservationResponse> findAll() {
        return reservationQueryService.findAllFetchJoinThemeAndTime()
                .stream()
                .map(it -> ReservationResponse.from(it, it.getTheme(), it.getTime()))
                .toList();
    }

    public List<ReservationAdminResponse> findAllForAdmin() {
        return reservationQueryService.findAllFetchJoinThemeAndTime()
                .stream()
                .map(it -> ReservationAdminResponse.from(it,
                        it.getTheme(),
                        it.getTime(),
                        MemberResponse.from(it.getMemberId(), it.getName()))
                )
                .toList();
    }

    public List<MyReservationResponse> findAllCreatedByLoginMember(LoginMember loginMember) {
        long memberId = loginMember.id();
        List<MyReservationResponse> reservationList = reservationQueryService.findAllByMemberId(memberId)
                .stream()
                .map(it -> MyReservationResponse.from(it, it.getTheme(), it.getTime()))
                .collect(Collectors.toList());
        List<MyReservationResponse> waitingList = reservationWaitingQueryService.findAllWithPositionByMemberId(memberId)
                .stream()
                .map(MyReservationResponse::fromWaitingResponse)
                .toList();

        reservationList.addAll(waitingList);
        reservationList.sort(Comparator.comparing(MyReservationResponse::date));

        return reservationList;
    }
}
