package roomescape.domain.reservation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.domain.Member;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.service.dto.ReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ApiReservationController {

    private final ReservationService reservationService;

    public ApiReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();
        return ResponseEntity.ok().body(reservationResponses);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getReservationsByMember(@Login Member loginMember) {
        List<ReservationResponse> myReservationResponses = reservationService.findAllByMemberId(loginMember.getId());
        return ResponseEntity.ok().body(myReservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@Login Member loginMember, @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.save(reservationRequest, loginMember);
        return ResponseEntity.ok().body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
