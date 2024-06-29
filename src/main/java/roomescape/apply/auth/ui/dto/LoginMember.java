package roomescape.apply.auth.ui.dto;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.ui.dto.MemberRoleNamesResponse;

public record LoginMember(
        long id,
        String name,
        String email,
        MemberRoleNamesResponse memberRoleNamesResponse
) {
    public static LoginMember from(Member member, MemberRoleNamesResponse memberRoleNamesResponse) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), memberRoleNamesResponse);
    }
}
