package com.wywin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    
    private String email;   // 이메일
    
    private String name;    // 이름
    
    private String nickName;    // 닉네임
    
    private Long point; // 포인트
}
