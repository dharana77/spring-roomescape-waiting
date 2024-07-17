package roomescape.apply.reservationwaiting.domain;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.util.List;

import static roomescape.apply.reservationwaiting.domain.QReservationWaiting.reservationWaiting;


public class CustomReservationWaitingRepositoryImpl implements CustomReservationWaitingRepository {

    private final JPAQueryFactory queryFactory;

    private static final NumberExpression<Long> WAITING_POSITION_EXPRESSION;

    static {
        final String template = "row_number() over(partition by {0}, {1}, {2} order by {3})";
        WAITING_POSITION_EXPRESSION = Expressions.numberTemplate(Long.class,
                template,
                reservationWaiting.theme.id,
                reservationWaiting.reservationDate.date,
                reservationWaiting.time.id,
                reservationWaiting.waitingTime);
    }

    public CustomReservationWaitingRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ReservationWaitingResponse> findAllWithPositionByMemberId(Long memberId) {
        final var queryResults = queryFactory.select(reservationWaiting, WAITING_POSITION_EXPRESSION)
                .from(reservationWaiting)
                .join(reservationWaiting.theme).fetchJoin()
                .join(reservationWaiting.time).fetchJoin()
                .where(reservationWaiting.memberId.eq(memberId))
                .orderBy(reservationWaiting.id.asc())
                .fetch();

        return queryResults.stream()
                .map(this::convertQueryResultToResponse)
                .toList();
    }

    private ReservationWaitingResponse convertQueryResultToResponse(Tuple result) {
        var rw = result.get(reservationWaiting);
        return ReservationWaitingResponse.from(rw,
                result.get(WAITING_POSITION_EXPRESSION),
                ThemeResponse.from(rw.getTheme()),
                ReservationTimeResponse.from(rw.getTime()));
    }
}
