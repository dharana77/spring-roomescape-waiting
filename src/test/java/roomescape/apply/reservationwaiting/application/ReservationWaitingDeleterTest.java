package roomescape.apply.reservationwaiting.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.application.handler.ReservationWaitingDeleter;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationWaitingFixture.reservationWaiting;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;


class ReservationWaitingDeleterTest {

    private ReservationWaitingDeleter reservationWaitingDeleter;

    private MemberRepository memberRepository;
    private ThemeRepository themeRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationWaitingRepository reservationWaitingRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        themeRepository = new InMemoryThemeRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(new InMemoryReservationRepository());
        reservationWaitingRepository = new InMemoryReservationWaitingRepository();

        reservationWaitingDeleter = new ReservationWaitingDeleter(new ReservationWaitingQueryService(reservationWaitingRepository),
                new ReservationWaitingCommandService(reservationWaitingRepository));
    }

    @Test
    @DisplayName("예약 대기를 삭제할 수 있다.")
    void deleteReservationWaiting() {
        // given
        Theme theme = themeRepository.save(theme());
        Member member1 = memberRepository.save(member());
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime());
        ReservationWaiting reservationWaiting = reservationWaiting(reservationTime, theme, member1.getId());
        ReservationWaiting save = reservationWaitingRepository.save(reservationWaiting);

        // when
        Long id = save.getId();
        assertThat(reservationWaitingRepository.findIdById(id)).isPresent();
        reservationWaitingDeleter.deleteReservationWaiting(id);

        // then
        assertThat(reservationWaitingRepository.findIdById(id)).isNotPresent();
    }

}
