package roomescape.apply.member.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.*;

class MemberRoleQueryServiceTest {

    private MemberRoleQueryService memberRoleQueryService;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberRoleRepository = new InMemoryMemberRoleRepository();

        memberRoleQueryService = new MemberRoleQueryService(memberRoleRepository);
    }

    @ParameterizedTest
    @MethodSource("provideMemberRoles")
    @DisplayName("사용자가 가진 모든 사용자 권한들을 찾을 수 있다.")
    void findRolesInMember(String email, Set<String> expectedRoles) {
        // given
        Member member = memberRepository.save(member(memberRequest(email)));
        memberRoleRepository.saveAll(expectedRoles.stream().map(it -> memberRole(it, member.getId())).iterator());
        // when
        Set<MemberRoleName> rolesInMember = new HashSet<>(memberRoleQueryService.findRolesInMember(member.getId()));
        // then
        assertThat(rolesInMember).isNotEmpty();
        Set<String> resultRoleName = rolesInMember.stream().map(MemberRoleName::getValue).collect(Collectors.toSet());
        assertThat(expectedRoles).isEqualTo(resultRoleName);
    }

    private static Stream<Arguments> provideMemberRoles() {
        return Stream.of(
                Arguments.of("admin@gmail.com", Set.of(MemberRoleName.ADMIN.getValue())),
                Arguments.of("guest@gmail.com", Set.of(MemberRoleName.GUEST.getValue())),
                Arguments.of("both@gmail.com", Set.of(MemberRoleName.ADMIN.getValue(), MemberRoleName.GUEST.getValue()))
        );
    }

}
