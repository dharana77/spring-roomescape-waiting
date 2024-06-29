package roomescape.apply.theme.intra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;

@SuppressWarnings("unused")
public interface ThemeJpaRepository extends ThemeRepository, JpaRepository<Theme, Long> {

}
