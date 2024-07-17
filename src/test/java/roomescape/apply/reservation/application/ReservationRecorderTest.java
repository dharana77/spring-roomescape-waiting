package roomescape.apply.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.application.handler.DuplicateReservationHandler;
import roomescape.apply.reservation.application.handler.ReservationRecorder;
import roomescape.apply.reservation.application.service.ReservationCommandService;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservation.ui.dto.CreateReservationResponse;
import roomescape.apply.reservation.ui.dto.CreatedReservationType;
import roomescape.apply.reservation.ui.dto.ReservationAdminRequest;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.loginMember;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationRecorderTest {

    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationRecorder reservationRecorder;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationWaitingRepository reservationWaitingRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        themeRepository = new InMemoryThemeRepository();
        memberRepository = new InMemoryMemberRepository();
        reservationWaitingRepository = new InMemoryReservationWaitingRepository();

        var duplicateReservationHandler = new DuplicateReservationHandler(new ReservationQueryService(reservationRepository),
                new ReservationWaitingCommandService(reservationWaitingRepository));
        reservationRecorder = new ReservationRecorder(new ThemeQueryService(themeRepository),
                new MemberQueryService(memberRepository),
                new ReservationCommandService(reservationRepository),
                new ReservationTimeQueryService(reservationTimeRepository),
                duplicateReservationHandler);
    }

    @Test
    @DisplayName("새로운 예약을 저장할 수 있다.")
    void recordReservationBy() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        ReservationRequest request = reservationRequest(time.getId(), theme.getId());
        Member save = memberRepository.save(member());
        // when
        CreateReservationResponse response = reservationRecorder.recordReservationBy(request, loginMember(save));
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response.statusCode()).isNotZero().isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.type()).isEqualTo(CreatedReservationType.RESERVATION);
        assertThat(reservationRepository.findOneById(response.id())).isPresent();
    }

    @Test
    @DisplayName("이미 예약된 경우 예약 대기가 생성된다.")
    void notDuplicatedTimeAndTheme() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        ReservationRequest request = reservationRequest(time.getId(), theme.getId());
        Member save = memberRepository.save(member());
        // when
        LoginMember loginMember = loginMember(save);
        reservationRecorder.recordReservationBy(request, loginMember);
        var response = reservationRecorder.recordReservationBy(request, loginMember);
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response.statusCode()).isNotZero().isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.type()).isEqualTo(CreatedReservationType.RESERVATION_WAITING);
        assertThat(reservationWaitingRepository.findIdById(response.id())).isPresent();
    }

    @Test
    @DisplayName("새로운 예약을 관리자가 저장할 수 있다.")
    void recordReservationAdminBy() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        Member save = memberRepository.save(member());
        ReservationAdminRequest request = reservationAdminRequest(time.getId(), theme.getId(), save.getId());
        // when
        CreateReservationResponse response = reservationRecorder.recordReservationBy(request);
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response.statusCode()).isNotZero().isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.type()).isEqualTo(CreatedReservationType.RESERVATION_ADMIN);
        assertThat(reservationRepository.findOneById(response.id())).isPresent();
    }

}
