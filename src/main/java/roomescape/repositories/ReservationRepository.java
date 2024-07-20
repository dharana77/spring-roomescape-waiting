package roomescape.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.entities.Reservation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  Reservation save(Reservation reservation);

  List<Reservation> findAll();

  Optional<Reservation> findByReservationTimeId(Long id);

  @Query(value = """
    SELECT r.id, r.name, r.date, rt.startAt as time
    FROM Reservation r
    JOIN r.reservationTime rt
    ON r.reservationTime.id = rt.id
    WHERE r.date = :date AND rt.startAt = :time
    """)
  Optional<Reservation> findByDateAndTime(String date, String time);

  void deleteById(Long id);
}
