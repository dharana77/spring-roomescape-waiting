package roomescape.apply.reservationwaiting.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.support.config.QueryDslConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;
import static roomescape.support.ReservationWaitingFixture.reservationWaiting;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

@DataJpaTest
@Import(QueryDslConfig.class)
@DisplayName("예약 대기 JPA 기반 테스트")
class ReservationWaitingQueryServiceTest {

    private ReservationWaitingQueryService reservationWaitingQueryService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationWaitingRepository reservationWaitingRepository;


    @BeforeEach
    void setUp() {
        reservationWaitingQueryService = new ReservationWaitingQueryService(reservationWaitingRepository);
    }

    @Test
    @DisplayName("사용자의 예약 대기들을 하나의 쿼리로 가져올 수 있다.")
    void findReservationWaitingListByMemberIdOneQuery() {
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
        var memberOneWaiting = reservationWaitingQueryService.findAllWithPositionByMemberId(member1.getId());
        var memberTwoWaiting = reservationWaitingQueryService.findAllWithPositionByMemberId(member2.getId());

        // then
        assertThat(memberOneWaiting).hasSize(2);
        assertThat(memberTwoWaiting).hasSize(6);
    }

    @Test
    @DisplayName("가장 오래된 예약 대기를 가져올 수 있다.")
    void findOldestWaitingBy() {
        // given
        Theme theme = themeRepository.save(theme());
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime());

        Member member1 = memberRepository.save(member());
        Member member2 = memberRepository.save(member(memberRequest("member2@gmail.com")));
        Member member3 = memberRepository.save(member(memberRequest("member3@gmail.com")));

        reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, "2099-12-02", member2.getId()));
        reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, "2099-12-02", member1.getId()));
        reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, "2099-12-02", member3.getId()));
        // when
        var oldestReservationWaiting = reservationWaitingQueryService.findOldestWaitingBy(theme.getId(),
                "2099-12-02",
                reservationTime.getId());
        var notExistReservationWaiting = reservationWaitingQueryService.findOldestWaitingBy(theme.getId(),
                "2002-12-31",
                reservationTime.getId());

        // then
        assertThat(oldestReservationWaiting).isPresent();
        assertThat(oldestReservationWaiting.get().getMemberId()).isEqualTo(member2.getId());
        assertThat(notExistReservationWaiting).isNotPresent();
    }


    @Test
    @DisplayName("예약 대기중인 순서들을 계산한다.")
    void calculateByReservationWaiting() {
        // given
        var theme = themeRepository.save(theme());
        var reservationTime = reservationTimeRepository.save(reservationTime());
        for (String emailId : List.of("member1", "member2", "member3", "member4", "member5", "member6")) {
            String email = emailId + "@gmail.com";
            sleep();
            Member member = memberRepository.save(member(memberRequest(email)));
            reservationWaitingRepository.save(reservationWaiting(reservationTime, theme, member.getId()));
        }

        // when && then
        long expectWaitingPosition = 0;
        for (String emailId : List.of("member1", "member2", "member3", "member4", "member5", "member6")) {
            Long memberId = memberRepository.findIdByEmail(emailId + "@gmail.com").orElseThrow();
            var waitingList = reservationWaitingRepository.findAllByMemberId(memberId);
            for (ReservationWaiting it : waitingList) {
                long actualPosition = reservationWaitingQueryService.calculateByReservationWaiting(it);
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
