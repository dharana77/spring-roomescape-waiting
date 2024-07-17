package roomescape.apply.reservation.ui.dto;

import org.springframework.http.HttpStatus;

public record CreateReservationResponse(
        int statusCode,
        long id,
        CreatedReservationType type
) {
    public static CreateReservationResponse createdReservation(long id) {
        return new CreateReservationResponse(HttpStatus.CREATED.value(), id, CreatedReservationType.RESERVATION);
    }

    public static CreateReservationResponse createdReservationAdmin(long id) {
        return new CreateReservationResponse(HttpStatus.CREATED.value(), id, CreatedReservationType.RESERVATION_ADMIN);
    }

    public static CreateReservationResponse createdReservationWaiting(long id) {
        return new CreateReservationResponse(HttpStatus.CREATED.value(), id, CreatedReservationType.RESERVATION_WAITING);
    }
}
