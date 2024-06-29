package roomescape.apply.member.domain;

import jakarta.persistence.*;

@Entity
public class MemberRole {

    @Id @Column(name = "member_role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private MemberRoleName memberRoleName;
    private Long memberId;

    protected MemberRole() {

    }

    public static MemberRole of(String roleValue, Long memberId) {
        MemberRole member = new MemberRole();
        member.memberRoleName = MemberRoleName.findRoleByValue(roleValue);
        member.memberId = memberId;
        return member;
    }

    public void changeId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public MemberRoleName getMemberRoleName() {
        return memberRoleName;
    }

    public Long getMemberId() {
        return memberId;
    }
}
