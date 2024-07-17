package roomescape.apply.theme.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeFinder {

    private final ThemeQueryService themeQueryService;

    public ThemeFinder(ThemeQueryService themeQueryService) {
        this.themeQueryService = themeQueryService;
    }

    public List<ThemeResponse> findAll() {
        return themeQueryService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
