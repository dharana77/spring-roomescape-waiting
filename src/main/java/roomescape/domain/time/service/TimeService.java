package roomescape.domain.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.domain.repository.TimeRepository;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;
import roomescape.domain.time.service.dto.TimeRequest;
import roomescape.domain.time.service.dto.TimeResponse;

import java.util.List;

import static roomescape.domain.time.utils.FormatCheckUtil.startAtFormatCheck;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @Transactional
    public TimeResponse save(TimeRequest timeRequest) {
        validationCheck(timeRequest.getStartAt());
        Time time = new Time(null, timeRequest.getStartAt());
        Long id = timeRepository.save(time);
        Time savedTime = findById(id);
        return mapToTimeResponseDto(savedTime);
    }

    @Transactional
    public Time findById(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new TimeException(TimeErrorCode.INVALID_TIME_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<TimeResponse> findAll() {
        List<Time> times = timeRepository.findAll();
        return times.stream().map(this::mapToTimeResponseDto).toList();
    }

    @Transactional
    public void delete(Long id) {
        Time time = timeRepository.findById(id).orElseThrow(() -> new TimeException(TimeErrorCode.INVALID_TIME_DETAILS_ERROR));
        timeRepository.delete(time);
    }

    @Transactional(readOnly = true)
    public List<TimeResponse> findByThemeIdAndDate(String themeId, String date) {
        List<Time> times = timeRepository.findByThemeIdAndDate(themeId, date);
        return times.stream().map(this::mapToTimeResponseDto).toList();
    }

    private void validationCheck(String startAt) {
        startAtFormatCheck(startAt);
    }

    private TimeResponse mapToTimeResponseDto(Time time) {
        return new TimeResponse(
                time.getId(),
                time.getStartAt()
        );
    }
}
