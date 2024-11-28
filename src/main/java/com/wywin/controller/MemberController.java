package com.wywin.controller;

import com.wywin.dto.MemberDTO;
import com.wywin.dto.MemberUpdateDTO;
import com.wywin.dto.UpdatePasswordDTO;
import com.wywin.entity.Member;
import com.wywin.repository.MemberRepository;
import com.wywin.service.EmailServiceImpl;
import com.wywin.service.KakaoService;
import com.wywin.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailServiceImpl emailService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

    @Value("${kakao.client.id}")
    private String client_id;

    @Value("${kakao.redirect.uri}")
    private String redirect_uri;


    @GetMapping(value = "/new")
    public String memberForm(Model model){/* 회원 가입 페이지로 이동할 수 있도록 MemberController클래스에 메소드를 작성*/
        log.info("MemberController - new(GET) --------------------------------------- ");
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/sign_up";
    }

    @PostMapping(value = "/emailCheck")
    @ResponseBody
    public String emailCheck(@RequestParam String email) {
        log.info("email 중복 체크");
        boolean result = memberService.existByEmail(email);
        log.info("중복체크 결과 : " + result);

        if(result == true) {
            return "fail"; // 중복 아이디 존재
        } else {
            return "success"; // 중복 아이디 존재 X
        }
    }

    @PostMapping(value = "/nickNameCheck")
    @ResponseBody
    public String nickNameCheck(@RequestParam String nickName) {
        log.info("email 중복 체크");
        boolean result = memberService.existByNickName(nickName);
        log.info("중복체크 결과 : " + result);

        if(result == true) {
            return "fail"; // 중복 아이디 존재
        } else {
            return "success"; // 중복 아이디 존재 X
        }
    }

    @PostMapping(value = "/new") // 회원 가입 POST 메서드
    public String newMember(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model){
        log.info("MemberController - new(POST) --------------------------------------- ");

        log.info("service.saveMember-------------------------------------------------------------");
        Member member = Member.createMember(memberDTO, passwordEncoder);
        memberService.saveMember(member);
        log.info(member);

        return "redirect:/";
    }

    @GetMapping(value = "/login") // 로그인 페이지 가져옴
    public String loginMember(Model model) {

        String location = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id="+client_id +
                "&redirect_uri="+redirect_uri +
                "response_type=code";
        model.addAttribute("location", location);

        // https://accounts.kakao.com/login/?
        // continue=https%3A%2F%2Fkauth.kakao.com%2Foauth%2Fauthorize%3F
        // client_id%3Ded0bd02823aab29e326bda173d1c8591%2520%26
        // redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A80%252Fkakao%252Fcallback
        // response_type%253Dcode%26through_account%3Dtrue#login

        // KOE101 - 잘못된 앱 키 오류
        return "member/login";
    }

    @GetMapping(value = "/login/error") // 로그인 오류시 처리
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }

    @GetMapping(value = "/myPage") // 마이페이지 가져옴
    public String myPage(Principal principal, Model model) {
        String memberInfo = principal.getName();
        log.info("MemberInfo : " + memberInfo);
        Member member = memberRepository.findByEmail(memberInfo);
        model.addAttribute("member", member);

        log.info("MyPageMemberInfo : " + member);
        // MyPageMemberInfo : Member(id=4, email=admin@test.com,
        // password=$2a$10$YbPpBjdcsy.iGlfrNID/luJCrSzdLOVvqNbMXDBAmE8ofM5idHZ1O,
        // name=admin, phoneNum=01012341234, address=tttt, nickName=123123, role=ADMIN, enabled=false)

        return "member/profile";
    }

    @GetMapping(value = "/update") // 정보수정 페이지
    public String updateForm(Principal principal, Model model) {
        String memberInfo = principal.getName();
        Member member = memberRepository.findByEmail(memberInfo);
        model.addAttribute("member", member);

        log.info("Member : " + member);

        return "member/updateForm";
    }

    @PostMapping(value = "/update") // 정보수정 처리
    public String updateMember(@Valid MemberUpdateDTO memberUpdateDTO, Model model) {
        model.addAttribute("member", memberUpdateDTO);
        memberService.updateMember(memberUpdateDTO);

        log.info("UpdateMember : " + memberUpdateDTO);

        return "redirect:/members/myPage";
    }

    @GetMapping(value = "/updatePassword") // 비밀번호 변경 페이지
    public String showChangePasswordPage() {
        log.info("MemberController - changePassword(GET) --------------------------------------- ");
        return "member/updatePassword";
    }

    @PostMapping(value = "/updatePassword")
    public String checkPassword(@Valid UpdatePasswordDTO updatePasswordDTO, Member member, Model model, Principal principal) throws Exception {
        String memberInfo = principal.getName();
        Member updateMember = memberRepository.findByEmail(memberInfo);
        log.info("MemberInfo : " + memberInfo);
        log.info("updateMember : " + updateMember);

        if(updateMember == null) {
            model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
            return "member/updatePassword";
        }

        /* 현재 비밀번호 확인
        if(!passwordEncoder.matches(currentPassword, updateMember.getPassword())) {
            log.info(passwordEncoder.matches(currentPassword, updateMember.getPassword()));
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "member/updatePassword";
        } */

        // 새 비밀번호와 비밀번호 확인 일치 여부 확인
        if(!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getNewPasswordChk())) {
            log.info("새 비밀번호 : " + updatePasswordDTO.getNewPassword());
            log.info("새 비밀번호 확인 : " + updatePasswordDTO.getNewPasswordChk());

            model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            return "member/updatePassword";
        }

        String encodedPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
        log.info("인코딩 패스워드 : " + encodedPassword);
        member.setPassword(encodedPassword);

        try{
            memberService.updatePassword(updatePasswordDTO, updateMember);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/updatePassword";
        }

        return "redirect:/members/myPage";
    }

    @PostMapping(value = "/emailConfirm")
    public String emailConfirm(@RequestParam String email) throws Exception {
        String confirm = emailService.sendSimpleMessage(email);

        return confirm;
    }

    @GetMapping(value = "/deleteID")
    public String deleteId() {
        return "member/deleteID";
    }

    @PostMapping(value = "/deleteID")
    public String memberDeleteId(@RequestParam String password, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean result = memberService.deleteID(userDetails.getUsername(), password);

        if(result) {
            return "redirect:/logout";
        } else {
            model.addAttribute("wrongPassword", "비밀번호가 일치하지 않습니다.");
            return "member/deleteID";
        }
    }

/*
    @PostMapping(value = "/changePassword")
    public String updatePassword(@ModelAttribute Member member, String currentPassword, String password, String passwordChk, Model model, Principal principal){
        log.info("MemberController - changePassword(POST) --------------------------------------- ");
        String memberInfo = principal.getName();
        Member updateMember = memberRepository.findByEmail(memberInfo);

        if(updateMember == null) {
            model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if(!passwordEncoder.matches(currentPassword, updateMember.getPassword())) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "member/changePassword";
        }

        // 새 비밀번호와 비밀번호 확인 일치 여부 확인
        if(!password.equals(passwordChk)) {
            model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            return "member/changePassword";
        }

        String encodedPassword = passwordEncoder.encode(password);
        member.setPassword(encodedPassword);

        try{
            memberService.updatePassword(member);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/changePassword";
        }

        return "redirect:/logout";
    }
*/

}
