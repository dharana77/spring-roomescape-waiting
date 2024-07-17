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
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;
import static roomescape.support.ReservationWaitingFixture.reservationWaiting;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

class ReservationWaitingFinderTest {

    private ReservationWaitingFinder reservationWaitingFinder;

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

        var waitingPositionCalculator = new WaitingPositionCalculator(reservationWaitingRepository);
        reservationWaitingFinder = new ReservationWaitingFinder(waitingPositionCalculator, reservationWaitingRepository);
    }

    @Test
    @DisplayName("사용자의 예약 대기들을 가져올 수 있다.")
    void a() {
        // given
        Theme theme = themeRepository.save(theme());
        Member member1 = memberRepository.save(member());
        Member member2 = memberRepository.save(member(memberRequest("member2@gmail.com")));
        int idx = 0;
        for (String time : List.of("09:00", "10:00", "11:00", "12:00", "13:00", "14:00")) {
            idx++;
            ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(time));
            if (idx > 4) {
                reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, member1.getId()));
                reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, member2.getId()));
            } else {
                reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, member2.getId()));
            }
        }
        // when
        var memberOneWaiting = reservationWaitingFinder.findReservationWaitingListByMemberId(member1.getId());
        var memberTwoWaiting = reservationWaitingFinder.findReservationWaitingListByMemberId(member2.getId());

        // then
        assertThat(memberOneWaiting).hasSize(2);
        assertThat(memberTwoWaiting).hasSize(6);
    }

}
