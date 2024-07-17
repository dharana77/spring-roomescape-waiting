package roomescape.apply.member.application.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.application.service.MemberQueryService;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberDuplicateCheckerTest {

    private MemberDuplicateChecker memberDuplicateChecker;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberDuplicateChecker = new MemberDuplicateChecker(new MemberQueryService(memberRepository));
    }

    @Test
    @DisplayName("이메일은 중복 될 수 없다.")
    void checkIsDuplicateEmail() {
        // given
        var request = memberRequest();
        Member member = member(request);
        // when
        memberRepository.save(member);
        // then
        assertThatThrownBy(() -> memberDuplicateChecker.checkIsDuplicateEmail(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일 입니다.");
    }

}
