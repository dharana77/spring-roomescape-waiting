package roomescape.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import roomescape.exceptions.ErrorCode;
import roomescape.exceptions.RoomEscapeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Entity
public class Reservation {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String date;

  @OneToOne
  @JoinColumn(name = "reservation_time_id")
  private ReservationTime reservationTime;

  @OneToOne
  @JoinColumn(name = "theme_id")
  private Theme theme;

  public Reservation() {
  }

  public Reservation(Long id, String name, String date, ReservationTime reservationTime, Theme theme) {
    if (!isValidReservedDateTime(date, reservationTime)){
      throw new RoomEscapeException(ErrorCode.INVALID_INPUT_VALUE, "예약 날짜가 현재 시간보다 이전입니다.");
    }
    this.id = id;
    this.name = name;
    this.date = date;
    this.reservationTime = reservationTime;
    this.theme = theme;
  }
  public Reservation(String name, String date, ReservationTime reservationTime, Theme theme) {
    this.name = name;
    this.date = date;
    this.reservationTime = reservationTime;
    this.theme = theme;
  }
  public Long getId() {
    return this.id;
  }

  public String getName(){
    return this.name;
  }

  public ReservationTime getReservationTime(){
    return this.reservationTime;
  }

  public String getDate(){
    return this.date;
  }

  public Theme getTheme(){ return this.theme; }

  public boolean isValidReservedDateTime(String date, ReservationTime reservationTime){
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime reservedDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(reservationTime.getStartAt()));
    return now.isBefore(reservedDateTime);
  }
}
