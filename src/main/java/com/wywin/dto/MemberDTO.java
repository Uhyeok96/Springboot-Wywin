package com.wywin.dto;

import com.wywin.constant.Role;
import com.wywin.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

@Getter
@Setter
public class MemberDTO {
    /*회원가입화면으로부터 넘어오는 가입정보를 담을 dto 생성*/

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "이메일 인증 코드는 필수 입력 값입니다.")
    private String authCode;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    /*@Pattern(regexp = "(?=.[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")*/
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 값입니다.")
    /*@Pattern(regexp = "(?=.[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")*/
    private String passwordCheck;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    /*@Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")*/
    private String phoneNum;

    /*@NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String zipcode; // 우편주소
    private String address1;
    private String address2;
    private String extraAddress; // 참고사항*/

    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$",
            message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickName;

    // 마일리지 계좌를 포함하는 필드 추가
    private MileageAccountDTO mileageAccount;

    @Builder.Default
    private int balance=1000;


}
