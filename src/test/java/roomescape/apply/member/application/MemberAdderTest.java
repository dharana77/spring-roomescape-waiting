package roomescape.apply.member.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRoleRepository;
import roomescape.apply.member.ui.dto.MemberResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.apply.member.domain.MemberRoleName.ADMIN;
import static roomescape.apply.member.domain.MemberRoleName.GUEST;
import static roomescape.support.MemberFixture.memberRequest;

class MemberAdderTest {

    private MemberAdder memberAdder;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberRoleRepository = new InMemoryMemberRoleRepository();

        var passwordHasher = new MockPasswordHasher();
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberRoleSaver = new MemberRoleSaver(memberRoleRepository);
        var memberDuplicateChecker = new MemberDuplicateChecker(new MemberFinder(passwordHasher, memberRepository, memberRoleFinder));
        memberAdder = new MemberAdder(memberRepository,
                passwordHasher,
                memberDuplicateChecker,
                memberRoleSaver
        );
    }

    @Test
    @DisplayName("새로운 사용자를 추가한다.")
    void saveMemberTest() {
        // given
        var request = memberRequest();
        // when
        MemberResponse memberResponse = memberAdder.addNewMember(request);
        // then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.name()).isEqualTo(request.name());
        var member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow();
        List<MemberRoleName> savedRoleNames = memberRoleRepository.findNamesByMemberId(member.getId());
        assertThat(savedRoleNames).isNotNull().hasSize(2)
                .containsExactlyInAnyOrder(GUEST, ADMIN);
    }

}
