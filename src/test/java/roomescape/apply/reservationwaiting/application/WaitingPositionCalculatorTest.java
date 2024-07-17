package roomescape.apply.reservationwaiting.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;
import static roomescape.support.ReservationWaitingFixture.reservationWaiting;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

class WaitingPositionCalculatorTest {

    private WaitingPositionCalculator waitingPositionCalculator;

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

        waitingPositionCalculator = new WaitingPositionCalculator(reservationWaitingRepository);
    }

    @Test
    @DisplayName("예약 대기중인 순서들을 계산한다.")
    void calculateByReservationWaiting() {
        // given
        var theme = themeRepository.save(theme());
        var reservationTime = reservationTimeRepository.save(reservationTime());
        for (String emailId : List.of("member1", "member2", "member3", "member4", "member5", "member6")) {
            String email = emailId + "@gmail.com";
            Member member = memberRepository.save(member(memberRequest(email)));
            reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, member.getId()));
            sleep();
        }

        // when && then
        long expectWaitingPosition = 0;
        for (String emailId : List.of("member1", "member2", "member3", "member4", "member5", "member6")) {
            Long memberId = memberRepository.findIdByEmail(emailId + "@gmail.com").orElseThrow();
            var waitingList = reservationWaitingRepository.findAllByMemberId(memberId);
            for (ReservationWaiting it : waitingList) {
                long actualPosition = waitingPositionCalculator.calculateByReservationWaiting(it);
                assertThat(actualPosition).isEqualTo(expectWaitingPosition);
            }

            expectWaitingPosition++;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
