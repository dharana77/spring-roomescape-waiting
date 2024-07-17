package roomescape.apply.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.auth.application.exception.IllegalTokenException;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private static final String LOGIN_FAIL_MESSAGE = "아이디 혹은 비밀번호가 잘못되었습니다.";

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findOneByEmailAndPassword(String email, String hashPassword) {
        return memberRepository.findByEmailAndPassword(email, hashPassword)
                .orElseThrow(() -> new IllegalArgumentException(LOGIN_FAIL_MESSAGE));
    }

    public boolean isAlreadyExistEmail(String email) {
        return memberRepository.findIdByEmail(email).isPresent();
    }

    public Member findOneByEmail(String email) {
        return memberRepository.findOneByEmail(email)
                .orElseThrow(() -> new IllegalTokenException("이메일이 존재하지 않습니다. 다시 로그인해주세요."));
    }

    public Member findOneById(long memberId) {
        return memberRepository.findOneById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("맴버가 존재하지 않습니다"));
    }
}
