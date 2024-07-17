package roomescape.apply.theme.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.theme.application.service.ThemeCommandService;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.ui.dto.ThemeRequest;
import roomescape.apply.theme.ui.dto.ThemeResponse;

@Service
public class ThemeSaver {

    private final ThemeCommandService themeCommandService;

    public ThemeSaver(ThemeCommandService themeCommandService) {
        this.themeCommandService = themeCommandService;
    }

    @Transactional
    public ThemeResponse saveThemeBy(ThemeRequest request) {
        final Theme theme = Theme.of(request.name(), request.description(), request.thumbnail());
        final Theme saved = themeCommandService.save(theme);
        return ThemeResponse.from(saved);
    }

}
