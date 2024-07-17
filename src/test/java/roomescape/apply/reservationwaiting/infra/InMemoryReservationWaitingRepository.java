package roomescape.apply.reservationwaiting.infra;

import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReservationWaitingRepository implements ReservationWaitingRepository {

    private final Map<Long, ReservationWaiting> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public ReservationWaiting save(ReservationWaiting reservationWaiting) {
        Long id = reservationWaiting.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            reservationWaiting.changeId(id);
        }
        map.put(id, reservationWaiting);
        return reservationWaiting;
    }

    @Override
    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        return map.values().stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .toList();
    }

    @Override
    public long countOtherWaitingByThemeDateTimeAndWaitingTime(long id,
                                                               long themeId,
                                                               String date,
                                                               long timeId,
                                                               LocalDateTime waitingTime
    ) {
        return map.values().stream()
                  .filter(it -> it.getId() != id)
                  .filter(it -> it.getTheme().getId() == themeId)
                  .filter(it -> it.getReservationDate().equals(date))
                  .filter(it -> it.getTime().getId() == timeId)
                  .filter(it -> it.getWaitingTime().isBefore(waitingTime))
                  .count();
    }

    @Override
    public List<ReservationWaitingResponse> findAllWithPositionByMemberId(Long memberId) {
        List<ReservationWaiting> memberReservation = map.values().stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .toList();

        Map<Long, Integer> waitingPositionMap = new HashMap<>();

        for (ReservationWaiting selected : memberReservation) {
            int position = calculatePosition(selected);
            waitingPositionMap.put(selected.getId(), position);
        }


        return memberReservation.stream()
                .map(reservation -> {
                    int waitingPosition = waitingPositionMap.get(reservation.getId());
                    ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());
                    ReservationTimeResponse timeResponse = ReservationTimeResponse.from(reservation.getTime());
                    return ReservationWaitingResponse.from(reservation, waitingPosition, themeResponse, timeResponse);
                })
                .toList();
    }

    @Override
    public Optional<Long> findIdById(long id) {
        if (map.containsKey(id)) {
            return Optional.of(id);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {
        map.remove(id);
    }

    @Override
    public Optional<ReservationWaiting> findOldestWaitingByThemeIdAndDateAndTimeId(long themeId, String date, long timeId) {
        return map.values().stream()
                .filter(it -> it.getTheme().getId() == themeId)
                .filter(it -> it.getReservationDate().equals(date))
                .filter(it -> it.getTime().getId() == timeId)
                .findFirst();
    }

    private int calculatePosition(ReservationWaiting selected) {
        int position = 0;
        for (ReservationWaiting each : map.values()) {
            boolean isRoomEscape = selected.getTheme().getId().equals(each.getTheme().getId())
                    && selected.getReservationDate().equals(each.getReservationDate())
                    && selected.getTime().getId().equals(each.getTime().getId());
            if (isRoomEscape && each.getWaitingTime().isBefore(selected.getWaitingTime())) {
                position++;
            }
        }
        return position;
    }
}
