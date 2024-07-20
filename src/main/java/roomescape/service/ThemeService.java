package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeAddRequestDto;
import roomescape.entities.Theme;
import roomescape.exceptions.ErrorCode;
import roomescape.exceptions.RoomEscapeException;
import roomescape.repositories.ThemeRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ThemeService {
  private final ThemeRepository themeRepository;

  public List<Theme> findAllThemes() {
    return themeRepository.findAll();
  }

  public Theme findById(Long id) {
    return themeRepository.findById(id).orElseThrow(() -> new RoomEscapeException(
      ErrorCode.NOT_FOUND,
      "Theme not found"
    ));
  }

  public Theme save(ThemeAddRequestDto themeAddRequestDto){
    Theme theme = new Theme(
      themeAddRequestDto.getName(),
      themeAddRequestDto.getDescription(),
      themeAddRequestDto.getThumbnail()
    );

    return themeRepository.save(theme);
  }

  public void deleteThemeById(Long id){
    themeRepository.deleteById(id);
  }
}
