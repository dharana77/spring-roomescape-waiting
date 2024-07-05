package roomescape.apply.reservation.ui.dto;

import java.util.Locale;
import java.util.ResourceBundle;

public class ReservationStatusMessages {

    private final ResourceBundle resourceBundle;

    public ReservationStatusMessages() {
        Locale defaultLocale = new Locale("ko");
        this.resourceBundle = ResourceBundle.getBundle("messages", defaultLocale);
    }

    public String waitingMessage() {
        return resourceBundle.getString("reservation.waiting");
    }

    public String reservedMessage() {
        return resourceBundle.getString("reservation.reserved");
    }

    public String canceledMessage() {
        return resourceBundle.getString("reservation.canceled");
    }
}
