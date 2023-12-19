package com.coviscon.memberservice.entity.member;

import com.coviscon.memberservice.dto.MemberRequestDto;
import com.coviscon.memberservice.entity.auditing.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email; // 이메일 (아이디)
    @Column(nullable = false)
    private String password; // 비번
    @Column(nullable = false)
    private String encryptedPassword; // 암호화 된 pw
    @Column(nullable = false)
    private String nickName; // 닉네임
    private String tel; // 전화 번호

    private String orderId; // UUID

    @Embedded
    private Mentor mentor;

    @Enumerated(EnumType.STRING)
    private Role role; // [STUDENT, TEACHER, MENTOR, MENTEE]

    // == 생성 메서드 ==
    public static Member createUser(MemberRequestDto memberRequestDto) {
        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(memberRequestDto.getPassword())
                .encryptedPassword(memberRequestDto.getEncryptedPassword())
                .nickName(memberRequestDto.getNickName())
                .role(setMemberRole(memberRequestDto.getRole()))
                .build();
    }

    private static Role setMemberRole(String role) {
        switch (role) {
            case "TEACHER":
                return Role.ROLE_TEACHER;
            case "MENTEE":
                return Role.ROLE_MENTEE;
            case "MENTOR":
                return Role.ROLE_MENTOR;
            default:
                return Role.ROLE_STUDENT;
        }
    }

    public void updatePassword(String nextPw) {
        this.password = nextPw;
    }

    public void updateEncryptedPassword(String nextPw) {
        this.encryptedPassword = nextPw;
    }

}
