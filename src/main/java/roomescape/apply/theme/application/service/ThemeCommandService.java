package roomescape.apply.theme.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;

@Service
@Transactional
public class ThemeCommandService {

    private final ThemeRepository themeRepository;

    public ThemeCommandService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void deleteById(long existId) {
        themeRepository.deleteById(existId);
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }
}
