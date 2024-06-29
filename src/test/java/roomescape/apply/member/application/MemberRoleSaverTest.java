package roomescape.apply.member.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.domain.*;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRoleRepository;

import java.util.List;
import java.util.Set;

import static roomescape.apply.member.domain.MemberRoleName.ADMIN;
import static roomescape.apply.member.domain.MemberRoleName.GUEST;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberRoleSaverTest {
    private MemberRoleSaver memberRoleSaver;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberRoleRepository = new InMemoryMemberRoleRepository();

        memberRoleSaver = new MemberRoleSaver(memberRoleRepository);
    }

    @Test
    @DisplayName("사용자는 하나 이상의 권한을 가질 수 있다.")
    void memberRoleSaverTest() {
        // given
        Member member = memberRepository.save(member(memberRequest()));
        Long memberId = member.getId();
        MemberRole guestRole = MemberRole.of("게스트", memberId);
        MemberRole adminRole = MemberRole.of("어드민", memberId);
        // when
        memberRoleSaver.saveAll(Set.of(guestRole, adminRole));
        // then
        List<MemberRoleName> roleNamesInMembers = memberRoleRepository.findNamesByMemberId(member.getId());
        Assertions.assertThat(roleNamesInMembers).isNotEmpty().hasSize(2)
                .containsExactlyInAnyOrder(GUEST, ADMIN);

    }

}
