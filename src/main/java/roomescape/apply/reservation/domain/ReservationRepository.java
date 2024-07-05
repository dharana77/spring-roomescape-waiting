package roomescape.apply.reservation.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    @Query("""
                SELECT
                    DISTINCT r
                FROM
                    Reservation r
                    JOIN FETCH r.theme
                    JOIN FETCH r.time
            """)
    List<Reservation> findAllFetchJoinThemeAndTime();

    Optional<Reservation> findOneById(long id);

    void deleteById(long id);

    @Query("""
                SELECT
                    r.id
                FROM
                    Reservation r
                WHERE
                    r.time.id = :timeId
                    AND r.theme.id = :themeId
                    AND r.reservationStatus = 'RESERVED'
            """)
    Optional<Long> findReservedIdByTimeIdAndThemeId(@Param("timeId") long timeId, @Param("themeId") long themeId);

    @Query("SELECT r.id FROM Reservation r WHERE r.time.id = :timeId")
    Optional<Long> findIdByTimeId(@Param("timeId") long timeId);

    @Query("SELECT r.id FROM Reservation r WHERE r.theme.id = :themeId")
    Optional<Long> findIdByThemeId(@Param("themeId") long themeId);

    List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams);

    @Query("SELECT r FROM Reservation r WHERE r.memberId = :memberId")
    List<Reservation> findAllByMemberId(@Param("memberId") long memberId);
}
