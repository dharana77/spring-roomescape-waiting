package roomescape.apply.theme.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme reservationTime);

    List<Theme> findAll();

    void deleteById(Long id);

    @Query("SELECT t.id FROM Theme t WHERE t.id = :id")
    Optional<Long> findIdById(@Param("id") long id);

    Optional<Theme> findOneById(long themeId);
}
