package roomescape.apply.member.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberQueryServiceTest {

    private MemberQueryService memberQueryService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberQueryService = new MemberQueryService(memberRepository);
    }


    @Test
    @DisplayName("중복된 이메일이 있는지 확인한다.")
    void isDuplicateEmail() {
        // given
        String targetEmail = "newbie1@gmail.com";
        memberRepository.save(member(memberRequest("newbie1", targetEmail)));
        // when
        boolean shouldTrue = memberQueryService.isAlreadyExistEmail(targetEmail);
        boolean shouldFalse = memberQueryService.isAlreadyExistEmail("NOT_EXIST@gmail.com");
        // then
        assertThat(shouldTrue).isTrue();
        assertThat(shouldFalse).isFalse();
    }
}
