package roomescape.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import roomescape.exceptions.ErrorCode;
import roomescape.exceptions.RoomEscapeException;

@Entity
public class ReservationTime {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String startAt;

  public ReservationTime() {
  }

  public ReservationTime(String startAt){
    if (!isValidStartAt(startAt)){
      throw new RoomEscapeException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 시간 형식입니다.");
    }
    this.startAt = startAt;
  }

  public ReservationTime(Long reservationTimeId, String startAt) {
    this.id = reservationTimeId;
    this.startAt = startAt;
  }

  public Long getId() {
    return this.id;
  }

  public String getStartAt() {
    return this.startAt;
  }

  private boolean isValidStartAt(String startAt){
    String[] times = startAt.split(":");
    int hour = Integer.parseInt(times[0]);
    int minute = Integer.parseInt(times[1]);

    if (hour >= 0 && hour <= 24 && minute >= 0 && minute <= 59){
      return true;
    }
    return false;
  }
}
