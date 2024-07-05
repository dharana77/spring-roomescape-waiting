package roomescape.apply.reservation.domain;

import roomescape.apply.reservation.ui.dto.ReservationStatusMessages;

public enum ReservationStatus {
    WAITING,
    RESERVED,
    CANCELED;

    public String toMessage() {
        final var reservationStatusMessages = new ReservationStatusMessages();
        return switch (this) {
            case WAITING -> reservationStatusMessages.waitingMessage();
            case RESERVED -> reservationStatusMessages.reservedMessage();
            case CANCELED -> reservationStatusMessages.canceledMessage();
        };
    }
}
