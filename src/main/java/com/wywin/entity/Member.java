package com.wywin.entity;

import com.wywin.constant.Role;
import com.wywin.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String email;   // 이메일

    private String pw;  // 비밀번호

    private String phoneNum;    // 연락처

    private String name;    // 이름

    @Column(unique = true)  // 유니크 속성 지정
    private String nickName;    // 닉네임

    private String address; // 주소

    private Long point; // 포인트(캐시역할)

    @Enumerated(EnumType.STRING)    // enum 타입을 엔티티의 속성으로 지정할 수 있음
    private Role role;  // constant.Role 사용자, 관리자 구분용


    public MemberDTO toDTO() {  // 엔티티를 dto로 변환
        MemberDTO dto = new MemberDTO();
        dto.setEmail(this.email);
        dto.setName(this.name);
        dto.setNickName(this.nickName);
        dto.setPoint(this.point);
        return dto;
    }

}
