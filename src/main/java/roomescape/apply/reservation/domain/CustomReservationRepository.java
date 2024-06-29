package roomescape.apply.reservation.domain;

import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;

public interface CustomReservationRepository {
    List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams);
}
