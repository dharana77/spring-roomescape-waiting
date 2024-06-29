package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationSearcher {

    private final ReservationRepository reservationRepository;

    public ReservationSearcher(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public List<ReservationAdminResponse> searchReservations(ReservationSearchParams searchParams) {
        List<Reservation> searchedReservations = reservationRepository.searchReservationsBySearchParams(searchParams);
        return searchedReservations
                .stream()
                .map(it -> ReservationAdminResponse.from(it,
                                                         it.getTheme(),
                                                         it.getTime(),
                                                         MemberResponse.from(it.getMemberId(), it.getName()))
                )
                .toList();
    }

}
