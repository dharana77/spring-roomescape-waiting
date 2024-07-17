package roomescape.apply.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.application.handler.ReservationFinder;
import roomescape.apply.reservation.application.service.ReservationQueryService;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.application.service.ReservationWaitingQueryService;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationFinderTest {

    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationFinder reservationFinder;
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

        reservationFinder = new ReservationFinder(new ReservationQueryService(reservationRepository),
                new ReservationWaitingQueryService(reservationWaitingRepository));
    }

    @Test
    @DisplayName("기존 예약들을 전부 가져올 수 있다.")
    void findAllTest() {
        // given
        Member saveMember = memberRepository.save(member());
        List<Reservation> reservations = Stream.of("10:00", "11:00", "12:00", "13:00", "14:00").map(time -> {
            ReservationTime saveReservationTime = reservationTimeRepository.save(reservationTime(time));
            Theme saveTheme = themeRepository.save(theme());
            return reservation(saveReservationTime, saveTheme, "2099-01-01", saveMember.getId());
        }).toList();

        for (Reservation reservation : reservations) {
            reservationRepository.save(reservation);
        }
        // when
        List<ReservationResponse> responses = reservationFinder.findAll();
        // then
        assertThat(responses).isNotEmpty().hasSize(reservations.size());
    }

}
