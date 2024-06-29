package roomescape.apply.member.infra;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return map.values().stream()
                .filter(member -> email.equals(member.getEmail()) && password.equals(member.getPassword()))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return map.values().stream()
                .filter(member -> email.equals(member.getEmail()))
                .map(Member::getId)
                .findAny();
    }

    @Override
    public Member save(Member member) {
        Long id = member.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            member.changeId(id);
        }
        map.put(id, member);
        return member;
    }

    @Override
    public Optional<Member> findOneByEmail(String email) {
        return map.values().stream()
                .filter(member -> email.equals(member.getEmail()))
                .findAny();
    }

    @Override
    public Optional<Member> findOneById(long memberId) {
        return Optional.ofNullable(map.get(memberId));
    }
}
