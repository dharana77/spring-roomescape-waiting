package roomescape.apply.reservation.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.reservation.application.service.ReservationCommandService;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;

import java.util.Optional;

@Service
public class ReservationCanceler {

    private final MemberQueryService memberQueryService;
    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;
    private final ReservationWaitingQueryService reservationWaitingQueryService;

    public ReservationCanceler(MemberQueryService memberQueryService,
                               ReservationQueryService reservationQueryService,
                               ReservationCommandService reservationCommandService,
                               ReservationWaitingQueryService reservationWaitingQueryService
    ) {
        this.memberQueryService = memberQueryService;
        this.reservationQueryService = reservationQueryService;
        this.reservationCommandService = reservationCommandService;
        this.reservationWaitingQueryService = reservationWaitingQueryService;
    }

    @Transactional
    public void cancelReservation(long id) {
        Reservation foundReservation = reservationQueryService.findOneById(id);
        foundReservation.cancel();
        reservationCommandService.save(foundReservation);
        Optional<ReservationWaiting> oldestWaiting = reservationWaitingQueryService.findOldestWaitingBy(
                foundReservation.getTheme().getId(),
                foundReservation.getReservationDate().value(),
                foundReservation.getTime().getId());

        oldestWaiting.ifPresent(reservationWaiting -> {
            final Member member = memberQueryService.findOneById(reservationWaiting.getMemberId());
            final Reservation reservation = Reservation.of(member.getName(),
                    reservationWaiting.getReservationDate(),
                    reservationWaiting.getTime(),
                    reservationWaiting.getTheme(),
                    member.getId());
            reservation.reserve();
            reservationCommandService.save(reservation);
        });
    }
}
