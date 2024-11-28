package com.wywin.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    private String email;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phoneNum;

    /*@NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String zipcode; // 우편주소
    private String address1;
    private String address2;
    private String extraAddress; // 참고사항*/

    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;
}
