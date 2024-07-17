package roomescape.apply.reservationtime.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;

@Service
@Transactional
public class ReservationTimeCommandService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeCommandService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }


    public void deleteById(long id) {
        reservationTimeRepository.deleteById(id);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeRepository.save(reservationTime);
    }
}
