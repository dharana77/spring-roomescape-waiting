package roomescape.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.entities.ReservationTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
  ReservationTime save(ReservationTime reservationTime);

  List<ReservationTime> findAll();

  void deleteById(Long id);

  Optional<ReservationTime> findById(Long id);

  @Query("""
    SELECT rt.id, rt.startAt 
    FROM ReservationTime rt 
    WHERE rt.id NOT IN (
        SELECT r.reservationTime.id 
        FROM Reservation r 
        WHERE r.date = :date AND r.theme.id = :themeId
    )
    """)
  List<ReservationTime> findAvailableTimes(String date, Long themeId);
}
