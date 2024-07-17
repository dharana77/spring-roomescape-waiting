package roomescape.apply.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;

@Service
@Transactional
public class MemberCommandService {
    private final MemberRepository memberRepository;

    public MemberCommandService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
