package roomescape.apply.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationCancelerTest {

    private ReservationCanceler reservationCanceler;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        themeRepository = new InMemoryThemeRepository();
        memberRepository = new InMemoryMemberRepository();

        reservationCanceler = new ReservationCanceler(reservationRepository);
    }

    @Test
    @DisplayName("기존 예약을 취소할 수 있다.")
    void cancelTest() {
        // given
        Member saveMember = memberRepository.save(member());
        ReservationTime saveReservationTime = reservationTimeRepository.save(reservationTime());
        Theme saveTheme = themeRepository.save(theme());
        Reservation saved = reservationRepository.save(reservation(saveReservationTime, saveTheme, "2099-01-01", saveMember.getId()));
        assertThat(reservationRepository.findAllFetchJoinThemeAndTime().size()).isNotZero();
        // when
        reservationCanceler.cancelReservation(saved.getId());
        // then
        assertThat(reservationRepository.findAllFetchJoinThemeAndTime()).isEmpty();
    }

}
