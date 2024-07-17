package roomescape.apply.reservation.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;
import roomescape.apply.reservationtime.domain.QReservationTime;
import roomescape.apply.theme.domain.QTheme;

import java.util.List;

public class CustomReservationRepositoryImpl implements CustomReservationRepository {

    private final JPAQueryFactory queryFactory;

    public CustomReservationRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams) {
        QReservation reservation = QReservation.reservation;
        QReservationTime reservationTime = QReservationTime.reservationTime;
        QTheme theme = QTheme.theme;
        BooleanBuilder builder = new BooleanBuilder();

        if (searchParams.themeId() != null) {
            builder.and(theme.id.eq(searchParams.themeId()));
        }
        if (searchParams.memberId() != null) {
            builder.and(reservation.memberId.eq(searchParams.memberId()));
        }
        if (searchParams.dateFrom() != null) {
            builder.and(reservation.reservationDate.date.goe(searchParams.dateFrom()));
        }
        if (searchParams.dateTo() != null) {
            builder.and(reservation.reservationDate.date.loe(searchParams.dateTo()));
        }
        return queryFactory.selectFrom(reservation)
                           .join(reservation.theme, theme).fetchJoin()
                           .join(reservation.time, reservationTime).fetchJoin()
                           .where(builder)
                           .fetch();
    }
}
