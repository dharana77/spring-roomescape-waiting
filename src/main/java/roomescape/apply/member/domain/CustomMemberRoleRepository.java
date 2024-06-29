package roomescape.apply.member.domain;

import java.util.Iterator;

public interface CustomMemberRoleRepository {
    void saveAll(Iterator<MemberRole> memberRoles);
}
