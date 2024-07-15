package roomescape.domain.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.domain.repository.ThemeRepository;
import roomescape.domain.theme.error.exception.ThemeErrorCode;
import roomescape.domain.theme.error.exception.ThemeException;
import roomescape.domain.theme.service.dto.ThemeRequest;
import roomescape.domain.theme.service.dto.ThemeResponse;

import java.util.List;

import static roomescape.domain.theme.utils.FormatCheckUtil.*;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ThemeResponse save(ThemeRequest themeRequest) {
        validationCheck(themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        Theme theme = new Theme(null, themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        Long id = themeRepository.save(theme);
        Theme savedTheme = findById(id);
        return mapToThemeResponseDto(savedTheme);
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new ThemeException(ThemeErrorCode.INVALID_THEME_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream().map(this::mapToThemeResponseDto).toList();
    }

    @Transactional
    public void delete(Long id) {
        Theme theme = findById(id);
        themeRepository.delete(theme);
    }

    private static void validationCheck(String name, String description, String thumbnail) {
        themeNameFormatCheck(name);
        themeDescriptionCheck(description);
        themeThumbnailFormatCheck(thumbnail);
    }

    private ThemeResponse mapToThemeResponseDto(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
