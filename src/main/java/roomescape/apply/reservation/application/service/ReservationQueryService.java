package roomescape.apply.reservation.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.excpetion.NotFoundReservationException;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation findOneById(long id) {
        return reservationRepository.findOneById(id)
                .orElseThrow(NotFoundReservationException::new);
    }

    public List<Reservation> findAllFetchJoinThemeAndTime() {
        return reservationRepository.findAllFetchJoinThemeAndTime();
    }

    public boolean notExistsReservedReservationIn(long themeId, String date, long timeId) {
        return reservationRepository.findIdByThemeIdAndDateAndTimeId(timeId, date, themeId).isEmpty();
    }

    public boolean isTimeIdReferenced(long timeId) {
        return reservationRepository.findIdByTimeId(timeId).isPresent();
    }

    public boolean isThemeIdReferenced(long themeId) {
        return reservationRepository.findIdByThemeId(themeId).isPresent();
    }

    public List<Reservation> findAllByMemberId(long memberId) {
        return reservationRepository.findAllByMemberId(memberId);
    }

    public List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams) {
        return reservationRepository.searchReservationsBySearchParams(searchParams);
    }

}
