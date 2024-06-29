package roomescape.domain.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.domain.repository.ThemeJpaRepository;
import roomescape.domain.theme.domain.repository.ThemeRepository;
import roomescape.domain.theme.error.exception.ThemeErrorCode;
import roomescape.domain.theme.error.exception.ThemeException;
import roomescape.domain.theme.service.dto.ThemeRequest;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeRequest themeRequest) {
        Theme theme = new Theme(null, themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        Long id = themeRepository.save(theme);
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new ThemeException(ThemeErrorCode.INVALID_THEME_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Theme theme = findById(id);
        themeRepository.delete(theme);
    }
}
