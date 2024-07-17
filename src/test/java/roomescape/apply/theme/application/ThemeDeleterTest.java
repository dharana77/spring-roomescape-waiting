package roomescape.apply.theme.application;

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
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.theme.application.exception.ThemeReferencedException;
import roomescape.apply.theme.application.handler.ThemeDeleter;
import roomescape.apply.theme.application.service.ThemeCommandService;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

class ThemeDeleterTest {
    private ThemeDeleter themeDeleter;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new InMemoryThemeRepository();
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        memberRepository = new InMemoryMemberRepository();

        themeDeleter = new ThemeDeleter(new ThemeQueryService(themeRepository),
                new ThemeCommandService(themeRepository),
                new ReservationQueryService(reservationRepository));
    }

    @Test
    @DisplayName("기존 테마를 삭제할 수 있다.")
    void cancelTest() {
        // given
        Theme save = themeRepository.save(theme());
        assertThat(themeRepository.findAll().size()).isNotZero();
        // when
        themeDeleter.deleteThemeBy(save.getId());
        // then
        assertThat(themeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("예약하고 있는 테마는 삭제할 수 없어야 한다.")
    void canNotDeletedTest() {
        // given
        Member saveMember = memberRepository.save(member());
        Theme theme = themeRepository.save(theme());
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        // when
        reservationRepository.save(Reservation.of("사용중_테스트", "2999-12-31", time, theme, saveMember.getId()));
        // then
        Long themeId = theme.getId();
        assertThatThrownBy(() -> themeDeleter.deleteThemeBy(themeId))
                .isInstanceOf(ThemeReferencedException.class)
                .hasMessage(ThemeReferencedException.DEFAULT_MESSAGE);
    }

}
