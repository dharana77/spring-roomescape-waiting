package roomescape.apply.reservation.infra;

import org.apache.commons.lang3.StringUtils;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Reservation save(Reservation reservation) {
        Long id = reservation.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            reservation.changeId(id);
        }
        map.put(id, reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findAllFetchJoinThemeAndTime() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<Long> findIdById(long id) {
        return map.containsKey(id) ? Optional.of(id) : Optional.empty();
    }

    @Override
    public void deleteById(long id) {
        map.remove(id);
    }

    @Override
    public Optional<Long> findIdByTimeIdAndThemeId(long timeId, long themeId) {
        return map.values().stream()
                .filter(it -> timeId == it.getTime().getId())
                .filter(it -> themeId == it.getTheme().getId())
                .findAny()
                .map(Reservation::getId);
    }

    @Override
    public Optional<Long> findIdByTimeId(long timeId) {
        return map.values().stream()
                .filter(it -> timeId == it.getTime().getId())
                .findAny()
                .map(Reservation::getId);
    }

    @Override
    public Optional<Long> findIdByThemeId(long themeId) {
        return map.values().stream()
                .filter(it -> themeId == it.getTheme().getId())
                .findAny()
                .map(Reservation::getId);
    }

    @Override
    public List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams) {
        return map.values().stream()
                .filter(reservation -> searchParams.themeId() == null
                        || searchParams.themeId().equals(reservation.getTheme().getId()))
                .filter(reservation -> searchParams.memberId() == null
                        || searchParams.memberId().equals(reservation.getMemberId()))
                .filter(reservation -> StringUtils.isEmpty(searchParams.dateFrom())
                        || reservation.getReservationDate().isNotBefore(searchParams.dateFrom()))
                .filter(reservation -> StringUtils.isEmpty(searchParams.dateTo())
                        || reservation.getReservationDate().isNotAfter(searchParams.dateTo()))
                .toList();
    }

}
