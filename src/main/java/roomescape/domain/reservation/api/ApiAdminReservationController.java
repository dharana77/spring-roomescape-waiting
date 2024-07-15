package roomescape.domain.reservation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.service.dto.AdminReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class ApiAdminReservationController {

    private final ReservationService reservationService;

    public ApiAdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> adminSave(@RequestBody AdminReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.adminSave(reservationRequest);
        return ResponseEntity.ok().body(reservationResponse);
    }
}
