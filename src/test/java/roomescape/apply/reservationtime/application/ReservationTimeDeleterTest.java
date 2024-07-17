package roomescape.apply.reservationtime.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.application.exception.ReservationTimeReferencedException;
import roomescape.apply.reservationtime.application.handler.ReservationTimeDeleter;
import roomescape.apply.reservationtime.application.service.ReservationTimeCommandService;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

class ReservationTimeDeleterTest {
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeDeleter reservationTimeDeleter;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new InMemoryThemeRepository();
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        memberRepository = new InMemoryMemberRepository();

        reservationTimeDeleter = new ReservationTimeDeleter(new ReservationQueryService(reservationRepository),
                new ReservationTimeQueryService(reservationTimeRepository),
                new ReservationTimeCommandService(reservationTimeRepository));
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void cancelTest() {
        // given
        ReservationTime save = reservationTimeRepository.save(reservationTime());
        assertThat(reservationTimeRepository.findAll().size()).isNotZero();
        // when
        reservationTimeDeleter.deleteReservationTimeBy(save.getId());
        // then
        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("예약하고 있는 예약 시간은 삭제할 수 없어야 한다.")
    void canNotDeletedTest() {
        // given
        Member savedMember = memberRepository.save(member());
        Theme theme = themeRepository.save(theme());
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        // when
        reservationRepository.save(Reservation.of("사용중_테스트", "2999-12-31", time, theme, savedMember.getId()));
        // then
        Long timeId = time.getId();
        assertThatThrownBy(() -> reservationTimeDeleter.deleteReservationTimeBy(timeId))
                .isInstanceOf(ReservationTimeReferencedException.class)
                .hasMessage(ReservationTimeReferencedException.DEFAULT_MESSAGE);
    }
}
