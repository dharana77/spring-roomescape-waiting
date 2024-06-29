package roomescape.apply.member.infra;

import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRoleRepository implements MemberRoleRepository {

    private final Map<Long, MemberRole> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public void saveAll(Iterator<MemberRole> memberRoles) {
        memberRoles.forEachRemaining(this::save);
    }

    @Override
    public List<MemberRoleName> findNamesByMemberId(long memberId) {
        return map.values().stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .map(it -> it.getMemberRoleName())
                .toList();
    }

    public MemberRole save(MemberRole memberRole) {
        Long id = memberRole.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            memberRole.changeId(id);
        }
        map.put(id, memberRole);
        return memberRole;
    }
}
