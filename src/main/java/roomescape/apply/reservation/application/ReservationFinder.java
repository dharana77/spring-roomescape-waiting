package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.ui.dto.MyReservationResponse;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationwaiting.application.ReservationWaitingFinder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationFinder {

    private final ReservationRepository reservationRepository;
    public final ReservationWaitingFinder reservationWaitingFinder;

    public ReservationFinder(ReservationRepository reservationRepository,
                             ReservationWaitingFinder reservationWaitingFinder) {
        this.reservationRepository = reservationRepository;
        this.reservationWaitingFinder = reservationWaitingFinder;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllFetchJoinThemeAndTime()
                .stream()
                .map(it -> ReservationResponse.from(it, it.getTheme(), it.getTime()))
                .toList();
    }

    public List<ReservationAdminResponse> findAllForAdmin() {
        return reservationRepository.findAllFetchJoinThemeAndTime()
                .stream()
                .map(it -> ReservationAdminResponse.from(it,
                        it.getTheme(),
                        it.getTime(),
                        MemberResponse.from(it.getMemberId(), it.getName()))
                )
                .toList();
    }

    public boolean doesReservationExist(long timeId, long themeId) {
        Optional<Long> existedId = reservationRepository.findReservedIdByTimeIdAndThemeId(timeId, themeId);
        return existedId.isPresent();
    }

    public Optional<Long> findIdByTimeId(long timeId) {
        return reservationRepository.findIdByTimeId(timeId);
    }

    public Optional<Long> findIdByThemeId(long themeId) {
        return reservationRepository.findIdByThemeId(themeId);
    }

    public List<MyReservationResponse> findAllCreatedByLoginMember(LoginMember loginMember) {
        long memberId = loginMember.id();
        List<MyReservationResponse> reservationList = reservationRepository.findAllByMemberId(memberId)
                .stream()
                .map(it -> MyReservationResponse.from(it, it.getTheme(), it.getTime()))
                .collect(Collectors.toList());
        List<MyReservationResponse> waitingList = reservationWaitingFinder.findReservationWaitingListByMemberId(memberId)
                .stream()
                .map(MyReservationResponse::fromWaitingResponse)
                .toList();

        reservationList.addAll(waitingList);
        reservationList.sort(Comparator.comparing(MyReservationResponse::date));

        return reservationList;
    }

    public List<MyReservationResponse> findAllCreatedByLoginMemberV2(LoginMember loginMember) {
        long memberId = loginMember.id();
        List<MyReservationResponse> reservationList = reservationRepository.findAllByMemberId(memberId)
                .stream()
                .map(it -> MyReservationResponse.from(it, it.getTheme(), it.getTime()))
                .collect(Collectors.toList());
        List<MyReservationResponse> waitingList = reservationWaitingFinder.findReservationWaitingListByMemberIdOneQuery(memberId)
                .stream()
                .map(MyReservationResponse::fromWaitingResponse)
                .toList();

        reservationList.addAll(waitingList);
        reservationList.sort(Comparator.comparing(MyReservationResponse::date));

        return reservationList;
    }
}
