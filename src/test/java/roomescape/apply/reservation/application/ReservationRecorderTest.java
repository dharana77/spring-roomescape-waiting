package roomescape.apply.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.application.MemberRoleFinder;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRoleRepository;
import roomescape.apply.reservation.application.excpetion.DuplicateReservationException;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.loginMember;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationRecorderTest {

    private ReservationRecorder reservationRecorder;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        var reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        themeRepository = new InMemoryThemeRepository();
        memberRepository = new InMemoryMemberRepository();
        var memberRoleRepository = new InMemoryMemberRoleRepository();

        var themeFinder = new ThemeFinder(themeRepository);
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberFinder = new MemberFinder(new MockPasswordHasher(), memberRepository, memberRoleFinder);
        var reservationFinder = new ReservationFinder(reservationRepository);
        var reservationTimeFinder = new ReservationTimeFinder(reservationTimeRepository);
        reservationRecorder = new ReservationRecorder(reservationRepository,
                reservationTimeFinder,
                themeFinder,
                reservationFinder,
                memberFinder);
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
        ReservationResponse response = reservationRecorder.recordReservationBy(request, loginMember(save));
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response.date()).isEqualTo(request.date());
        assertThat(response.theme().id()).isEqualTo(request.themeId());
        assertThat(response.time().id()).isEqualTo(request.timeId());
        assertThat(response.name()).isNotEmpty();

    }

    @Test
    @DisplayName("이미 예약된 시간의 테마는 예약할 수 없어야한다.")
    void notDuplicatedTimeAndTheme() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        ReservationRequest request = reservationRequest(time.getId(), theme.getId());
        Member save = memberRepository.save(member());
        // when
        LoginMember loginMember = loginMember(save);
        reservationRecorder.recordReservationBy(request, loginMember);
        // then
        assertThatThrownBy(() -> reservationRecorder.recordReservationBy(request, loginMember))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage(DuplicateReservationException.DEFAULT_MESSAGE);
    }

}
