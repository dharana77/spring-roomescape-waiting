package roomescape.domain.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.domain.repository.TimeRepository;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;
import roomescape.domain.time.service.dto.TimeRequest;

import java.util.List;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @Transactional
    public Time save(TimeRequest timeRequest) {
        Time time = new Time(null, timeRequest.getStartAt());
        Long id = timeRepository.save(time);
        return findById(id);
    }

    @Transactional
    public Time findById(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new TimeException(TimeErrorCode.INVALID_TIME_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Time time = timeRepository.findById(id).orElseThrow(() -> new TimeException(TimeErrorCode.INVALID_TIME_DETAILS_ERROR));
        timeRepository.delete(time);
    }

    @Transactional(readOnly = true)
    public List<Time> findByThemeIdAndDate(String themeId, String date) {
        return timeRepository.findByThemeIdAndDate(themeId, date);
    }
}
