package roomescape.apply.member.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Iterator;
import java.util.List;

public interface MemberRoleRepository {

    void saveAll(Iterator<MemberRole> memberRoles);

    @Query("SELECT mr.memberRoleName FROM MemberRole mr WHERE mr.memberId = :memberId")
    List<MemberRoleName> findNamesByMemberId(@Param("memberId") long memberId);
}
