package roomescape.apply.reservationwaiting.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.application.handler.ReservationWaitingSaver;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingCommandService;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.loginMember;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationWaitingFixture.reservationWaitingRequest;
import static roomescape.support.ReservationsFixture.reservationTime;
import static roomescape.support.ReservationsFixture.theme;

class ReservationWaitingSaverTest {

    private ReservationWaitingSaver reservationWaitingSaver;

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

        reservationWaitingSaver = new ReservationWaitingSaver(new ThemeQueryService(themeRepository),
                new ReservationTimeQueryService(reservationTimeRepository),
                new ReservationWaitingQueryService(reservationWaitingRepository),
                new ReservationWaitingCommandService(reservationWaitingRepository));
    }

    @Test
    @DisplayName("사용자가 예약 대기를 만들 수 있다.")
    void saveNewReservationWaiting() {
        // given
        Theme theme = themeRepository.save(theme());
        Member member = memberRepository.save(member());
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime());

        // when
        Long timeId = reservationTime.getId();
        Long themeId = theme.getId();
        reservationWaitingSaver.saveReservationWaitingBy(reservationWaitingRequest(timeId, themeId), loginMember(member));

        // then
        var waitingList = reservationWaitingRepository.findAllByMemberId(member.getId());
        assertThat(waitingList).hasSize(1);
        waitingList.forEach(it -> assertThat(it.getWaitingTime()).isNotNull());
    }

}
