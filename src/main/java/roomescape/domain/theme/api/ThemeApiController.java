package roomescape.domain.theme.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.theme.service.dto.ThemeRequest;
import roomescape.domain.theme.service.dto.ThemeResponse;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> save(@RequestBody ThemeRequest themeRequest) {
        ThemeResponse themeResponse = themeService.save(themeRequest);
        return ResponseEntity.ok().body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.findAll();
        return ResponseEntity.ok(themeResponses);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
