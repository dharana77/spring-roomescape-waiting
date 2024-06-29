package roomescape.apply.theme.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.ui.dto.ThemeRequest;
import roomescape.apply.theme.ui.dto.ThemeResponse;

@Service
public class ThemeSaver {

    private final ThemeRepository themeRepository;

    public ThemeSaver(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ThemeResponse saveThemeBy(ThemeRequest request) {
        final Theme theme = Theme.of(request.name(), request.description(), request.thumbnail());
        final Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

}
