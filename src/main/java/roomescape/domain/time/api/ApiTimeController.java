package roomescape.domain.time.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.time.service.TimeService;
import roomescape.domain.time.service.dto.TimeRequest;
import roomescape.domain.time.service.dto.TimeResponse;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ApiTimeController {

    private final TimeService timeService;

    public ApiTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> save(@RequestBody TimeRequest timeRequest) {
        TimeResponse timeResponse = timeService.save(timeRequest);
        return ResponseEntity.ok().body(timeResponse);
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> findAll() {
        List<TimeResponse> timeResponses = timeService.findAll();
        return ResponseEntity.ok().body(timeResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<TimeResponse>> findByThemeIdAndDate(@RequestParam("date") String date, @RequestParam("themeId") String themeId) {
        List<TimeResponse> timeResponses = timeService.findByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(timeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        timeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
