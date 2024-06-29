package roomescape.apply.auth.ui.dto;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.ui.dto.MemberRoleNamesResponse;

public record LoginResponse(
        String email,
        String name,
        MemberRoleNamesResponse memberRoleNamesResponse
) {
    public static LoginResponse from(Member member, MemberRoleNamesResponse memberRoleNamesResponse) {
        return new LoginResponse(member.getEmail(), member.getName(), memberRoleNamesResponse);
    }
}
