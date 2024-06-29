package roomescape.apply.reservationtime.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.ReservationsFixture.reservationTimeRequest;

class ReservationTimeSaverTest {
    private ReservationTimeSaver reservationTimeSaver;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new InMemoryReservationTimeRepository(new InMemoryReservationRepository());
        reservationTimeSaver = new ReservationTimeSaver(reservationTimeRepository);
    }

    @Test
    @DisplayName("새로운 예약 시간을 저장할 수 있다.")
    void recordReservationBy() {
        // given
        assertThat(reservationTimeRepository.findAll()).isEmpty();
        ReservationTimeRequest request = reservationTimeRequest();
        // when
        ReservationTimeResponse response = reservationTimeSaver.saveReservationTimeBy(request);
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response.startAt()).isEqualTo(request.startAt());
    }

}
