package roomescape.apply.reservationtime.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    @Query("SELECT rt.id FROM ReservationTime rt WHERE rt.id = :id")
    Optional<Long> findIdById(@Param("id") long id);

    Optional<ReservationTime> findOneById(long timeId);

    @Query("""
            SELECT
                new roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse(
                    rt.id,
                    rt.startAt,
                    CASE
                        WHEN r.id IS NOT NULL THEN TRUE
                        ELSE FALSE
                    END
                )
            FROM
                ReservationTime rt
            LEFT JOIN
                Reservation r ON rt.id = r.time.id
                AND r.reservationDate.date = :date
                AND r.theme.id = :themeId
                AND r.reservationStatus = 'RESERVED'
            """)
    List<AvailableReservationTimeResponse> findAllReservedTimesByThemeIdAndDate(@Param("themeId") long themeId,
                                                                                @Param("date") String date);
}
