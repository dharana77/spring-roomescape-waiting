package roomescape.apply.theme.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.theme.application.exception.NotFoundThemeException;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeQueryService {

    private final ThemeRepository themeRepository;

    public ThemeQueryService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Long findIdById(long id) {
        return themeRepository.findIdById(id).orElseThrow(NotFoundThemeException::new);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme findOneById(long themeId) {
        return themeRepository.findOneById(themeId).orElseThrow(NotFoundThemeException::new);
    }
}
