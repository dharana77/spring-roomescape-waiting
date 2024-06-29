package roomescape.domain.time.domain.repository;

import roomescape.domain.time.domain.Time;

import java.util.List;
import java.util.Optional;

public interface TimeRepository {
    Long save(Time time);

    Optional<Time> findById(Long timeId);

    List<Time> findAll();

    void delete(Time time);

    List<Time> findByThemeIdAndDate(String themeId, String date);
}
