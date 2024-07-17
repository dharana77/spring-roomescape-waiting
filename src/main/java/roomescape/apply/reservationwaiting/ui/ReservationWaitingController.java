package roomescape.apply.reservationwaiting.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.reservationwaiting.application.handler.ReservationWaitingDeleter;
import roomescape.apply.reservationwaiting.application.handler.ReservationWaitingSaver;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingRequest;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;

@RestController
@RequestMapping("/reservation-waiting")
public class ReservationWaitingController {

    private final ReservationWaitingSaver reservationWaitingSaver;
    private final ReservationWaitingDeleter reservationWaitingDeleter;

    public ReservationWaitingController(ReservationWaitingSaver reservationWaitingSaver,
                                        ReservationWaitingDeleter reservationWaitingDeleter
    ) {
        this.reservationWaitingSaver = reservationWaitingSaver;
        this.reservationWaitingDeleter = reservationWaitingDeleter;
    }

    @PostMapping
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<ReservationWaitingResponse> addReservationWaiting(@RequestBody ReservationWaitingRequest request,
                                                                            LoginMember loginMember
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationWaitingSaver.saveReservationWaitingBy(request, loginMember));
    }

    @DeleteMapping("/{id}")
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<ReservationWaitingResponse> deleteReservationWaiting(@PathVariable Long id) {
        reservationWaitingDeleter.deleteReservationWaiting(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
