package roomescape.apply.theme.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.theme.application.handler.ThemeFinder;
import roomescape.apply.theme.application.service.ThemeQueryService;
import roomescape.apply.theme.infra.InMemoryThemeRepository;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.ReservationsFixture.theme;

class ThemeFinderTest {

    private ThemeFinder themeFinder;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new InMemoryThemeRepository();
        themeFinder = new ThemeFinder(new ThemeQueryService(themeRepository));
    }

    @Test
    @DisplayName("기존 테마들을 전부 가져올 수 있다.")
    void findAllTest() {
        // given
        List<String> themeNames = List.of("호그와트", "기숙사", "감옥", "폐가", "병원");
        for (String name : themeNames) {
            themeRepository.save(theme(name, name + "을 탈출하는 내용입니다."));
        }
        // when
        List<ThemeResponse> responses = themeFinder.findAll();
        // then
        assertThat(responses).isNotEmpty().hasSize(themeNames.size());
    }

}
