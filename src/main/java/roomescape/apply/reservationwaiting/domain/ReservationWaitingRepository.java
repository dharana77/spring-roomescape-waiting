package roomescape.apply.reservationwaiting.domain;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationWaitingRepository {
    ReservationWaiting save(ReservationWaiting reservationWaiting);

    @Query("SELECT rw FROM ReservationWaiting rw WHERE rw.memberId = :memberId")
    List<ReservationWaiting> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("""
            SELECT
                COUNT(rw)
             FROM
                ReservationWaiting rw
             WHERE
                rw.id != :id
                AND rw.theme.id = :themeId
                AND rw.reservationDate.date = :date
                AND rw.time.id = :timeId
                AND rw.waitingTime < :waitingTime
            """)
    long countOtherWaitingByThemeDateTimeAndWaitingTime(@Param("id") long id,
                                                        @Param("themeId") long themeId,
                                                        @Param("date") String date,
                                                        @Param("timeId") long timeId,
                                                        @Param("waitingTime") LocalDateTime waitingTime);

    List<ReservationWaitingResponse> findAllWithPositionByMemberId(Long memberId);

    @Query("SELECT rw.id FROM ReservationWaiting rw WHERE rw.id = :id")
    Optional<Long> findIdById(@Param("id") long id);

    void deleteById(long id);

    @Query("""
                SELECT
                    rw
                FROM
                    ReservationWaiting rw
                WHERE
                    rw.theme.id = :themeId
                    AND rw.reservationDate.date = :date
                    AND rw.time.id = :timeId
                ORDER BY
                    rw.waitingTime ASC
                LIMIT 1
            """)
    Optional<ReservationWaiting> findOldestWaitingByThemeIdAndDateAndTimeId(@Param("themeId") long themeId,
                                                                            @Param("date") String date,
                                                                            @Param("timeId") long timeId);
}
