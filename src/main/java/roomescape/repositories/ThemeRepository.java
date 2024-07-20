package roomescape.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entities.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

  Optional<Theme> findById(Long id);

  List<Theme> findAll();


  Theme save(Theme theme);

  void deleteById(Long id);
}
