package roomescape.apply.reservation.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.reservation.application.service.ReservationCommandService;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.ui.dto.CreateReservationResponse;
import roomescape.apply.reservation.ui.dto.ReservationAdminRequest;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.domain.Theme;

@Service
public class ReservationRecorder {

    private final ThemeQueryService themeQueryService;
    private final MemberQueryService memberQueryService;
    private final ReservationCommandService reservationCommandService;
    private final ReservationTimeQueryService reservationTimeQueryService;
    private final DuplicateReservationHandler duplicateReservationHandler;

    public ReservationRecorder(ThemeQueryService themeQueryService,
                               MemberQueryService memberQueryService,
                               ReservationCommandService reservationCommandService,
                               ReservationTimeQueryService reservationTimeQueryService,
                               DuplicateReservationHandler duplicateReservationHandler
    ) {
        this.themeQueryService = themeQueryService;
        this.memberQueryService = memberQueryService;
        this.reservationCommandService = reservationCommandService;
        this.reservationTimeQueryService = reservationTimeQueryService;
        this.duplicateReservationHandler = duplicateReservationHandler;
    }

    @Transactional
    public CreateReservationResponse recordReservationBy(ReservationRequest request, LoginMember loginMember) {
        final ReservationTime time = reservationTimeQueryService.findOneById(request.timeId());
        final Theme theme = themeQueryService.findOneById(request.themeId());

        final var responseOptional = duplicateReservationHandler.handleDuplicateReservation(theme, request.date(),
                time, loginMember.id());
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        }

        final Reservation reservation = Reservation.of(loginMember.name(), request.date(), time, theme, loginMember.id());
        reservation.reserve();
        final Reservation saved = reservationCommandService.save(reservation);

        return CreateReservationResponse.createdReservation(saved.getId());
    }

    @Transactional
    public CreateReservationResponse recordReservationBy(ReservationAdminRequest request) {
        final ReservationTime time = reservationTimeQueryService.findOneById(request.timeId());
        final Theme theme = themeQueryService.findOneById(request.themeId());
        final Member member = memberQueryService.findOneById(request.memberId());
        final var responseOptional = duplicateReservationHandler.handleDuplicateReservation(theme, request.date(),
                time, request.memberId());
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        }

        final Reservation reservation = Reservation.of(member.getName(), request.date(), time, theme, member.getId());
        reservation.reserve();
        final Reservation saved = reservationCommandService.save(reservation);

        return CreateReservationResponse.createdReservationAdmin(saved.getId());
    }

}
