package roomescape.apply.reservationtime.infra;

import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();
    private final ReservationRepository reservationRepository;

    public InMemoryReservationTimeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Long id = reservationTime.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            reservationTime.changeId(id);
        }
        map.put(id, reservationTime);
        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }


    @Override
    public Optional<Long> findIdById(long id) {
        return map.containsKey(id) ? Optional.of(id) : Optional.empty();
    }

    @Override
    public Optional<ReservationTime> findOneById(long themeId) {
        return Optional.ofNullable(map.get(themeId));
    }

    @Override
    public List<AvailableReservationTimeResponse> findAllReservedTimesByThemeIdAndDate(long themeId, String date) {
        var reservationTimes = map.values().stream().toList();
        var reservedTimeIds = reservationRepository.findAllFetchJoinThemeAndTime()
                .stream()
                .filter(it -> it.getReservationDate().value().equals(date))
                .filter(it -> it.getTheme().getId() == themeId)
                .map(it -> it.getTime().getId())
                .toList();

        return reservationTimes.stream()
                .map(it -> new AvailableReservationTimeResponse(it.getId(), it.getStartAt(), reservedTimeIds.contains(it.getId())))
                .toList();
    }
}
