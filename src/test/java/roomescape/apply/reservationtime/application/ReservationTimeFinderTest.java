package roomescape.apply.reservationtime.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationTimeFinderTest {

    private ReservationTimeFinder reservationTimeFinder;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        themeRepository = new InMemoryThemeRepository();
        memberRepository = new InMemoryMemberRepository();
        reservationTimeFinder = new ReservationTimeFinder(reservationTimeRepository);
    }

    @Test
    @DisplayName("저장한 예약 시간들을 전부 가져올 수 있다.")
    void findAllTest() {
        // given
        List<String> times = List.of("10:00", "11:00", "12:00", "13:00", "14:00");
        for (String time : times) {
            reservationTimeRepository.save(reservationTime(time));
        }
        // when
        List<ReservationTimeResponse> responses = reservationTimeFinder.findAll();
        // then
        assertThat(responses).isNotEmpty().hasSize(times.size());
    }

    @Test
    @DisplayName("테마의 시간들을 예약 여부와 함께 가져온다.")
    void asd() {
        // given
        Member saveMember = memberRepository.save(member());
        List.of("10:00", "12:00", "14:00", "16:00")
                .forEach(it -> reservationTimeRepository.save(reservationTime(it)));

        var reservationTime = reservationTime("18:00");
        reservationTimeRepository.save(reservationTime);
        String date = "2099-01-01";
        Theme savedTheme = themeRepository.save(theme());
        reservationRepository.save(reservation(reservationTime, savedTheme, date, saveMember.getId()));
        // when
        var responses = reservationTimeFinder.findAvailableTimesBy(date, savedTheme.getId().toString());
        // then
        assertThat(responses).isNotEmpty().hasSize(5);
        assertThat(responses).extracting("startAt")
                .containsExactlyInAnyOrder("10:00", "12:00", "14:00", "16:00", "18:00");
        assertThat(responses).extracting("alreadyBooked")
                .containsExactlyInAnyOrder(false, false, false, false, true);
    }

}
