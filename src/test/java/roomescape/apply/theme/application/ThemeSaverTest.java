package roomescape.apply.theme.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.theme.application.handler.ThemeSaver;
import roomescape.apply.theme.application.service.ThemeCommandService;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;
import roomescape.apply.theme.ui.dto.ThemeRequest;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.ReservationsFixture.themeRequest;

class ThemeSaverTest {
    private ThemeSaver themeSaver;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new InMemoryThemeRepository();
        themeSaver = new ThemeSaver(new ThemeCommandService(themeRepository));
    }

    @Test
    @DisplayName("새로운 테마를 저장할 수 있다.")
    void recordReservationBy() {
        // given
        assertThat(themeRepository.findAll()).isEmpty();
        ThemeRequest request = themeRequest();
        // when
        ThemeResponse response = themeSaver.saveThemeBy(request);
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(request);
    }

}
