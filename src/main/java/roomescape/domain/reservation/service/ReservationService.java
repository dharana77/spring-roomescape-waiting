package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.repository.ReservationRepository;
import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.service.dto.AdminReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationRequest;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.TimeService;

import java.util.List;

import static roomescape.utils.DateTimeCheckUtil.isBeforeCheck;
import static roomescape.utils.FormatCheckUtil.reservationDateFormatCheck;
import static roomescape.utils.FormatCheckUtil.reservationNameFormatCheck;

@Service
public class ReservationService {

    private final TimeService timeService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final ReservationRepository reservationRepository;

    public ReservationService(TimeService timeService, ThemeService themeService, MemberService memberService, ReservationRepository reservationRepository) {
        this.timeService = timeService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation save(ReservationRequest reservationRequest, Member loginMember) {
        Time time = timeService.findById(reservationRequest.getTimeId());
        Theme theme = themeService.findById(reservationRequest.getThemeId());
        validationCheck(reservationRequest.getName(), reservationRequest.getDate(), time);
        Reservation reservation = new Reservation(null, reservationRequest.getName(), reservationRequest.getDate(), theme, time, loginMember);
        Long id = reservationRepository.save(reservation);
        return findById(id);
    }

    @Transactional
    public Reservation adminSave(AdminReservationRequest adminReservationRequest) {
        Time time = timeService.findById(adminReservationRequest.getTimeId());
        Theme theme = themeService.findById(adminReservationRequest.getThemeId());
        Member member = memberService.findById(adminReservationRequest.getMemberId());
        Reservation reservation = new Reservation(null, member.getName(), adminReservationRequest.getDate(), theme, time, member);
        Long id = reservationRepository.save(reservation);
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Reservation findById(long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationException(ReservationErrorCode.INVALID_RESERVATION_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
    }

    private void validationCheck(String name, String date, Time time) {
        reservationNameFormatCheck(name);
        reservationDateFormatCheck(date);
        isBeforeCheck(date, time.getStartAt());
    }
}
