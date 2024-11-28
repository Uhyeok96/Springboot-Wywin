package com.wywin.controller;

import com.wywin.entity.Member;
import com.wywin.entity.MileageAccount;
import com.wywin.repository.MemberRepository;
import com.wywin.service.MemberService;

import com.wywin.service.MileageAccountService;
import com.wywin.service.MileageHistoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Log4j2
@Controller
@RequestMapping("/mileage")
public class MileageController {
    @Autowired
    private MileageAccountService mileageAccountService;

    @Autowired
    private MileageHistoryService mileageHistoryService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/myMileage")
    public String mileageHome(Model model, Principal principal){
        String memberInfo = principal.getName();
        log.info("MyMileage_MemberInfo : " + memberInfo);
        Member member = memberRepository.findByEmail(memberInfo);
        log.info("Member_M : " + member);
        model.addAttribute("member", member);

        return "mileage/myMileage";
    }

    // 마일리지 내역 조회
    @GetMapping("/history")
    public String viewHistory(Model model) {
        Member member = memberService.findMemberById(1L); // 로그인된 사용자
        model.addAttribute("historyList", mileageHistoryService.getHistoryForMember(member));
        return "mileage/mileageHistory";
    }

    // 마일리지 충전 폼
    @GetMapping("/chargeForm")
    public String chargeForm() {
        return "mileage/chargeMileage";
    }

    // 마일리지 충전 처리
    @PostMapping("/charge")
    public String chargeMileage(@RequestParam int amount) {
        Member member = memberService.findMemberById(1L);
        mileageAccountService.chargeMileage(member.getId(), amount);
        mileageHistoryService.logHistory(member, amount, "충전", "카카오페이 충전");
        return "redirect:/mileage/history"; // 충전 후 내역 페이지로 리다이렉트
    }

    // 마일리지 차감 폼
    @GetMapping("/deductForm")
    public String deductForm() {
        return "mileage/mileageDeduct";
    }

    // 마일리지 차감 처리
    @PostMapping("/deduct")
    public String deductMileage(@RequestParam int amount) {
        Member member = memberService.findMemberById(1L);
        boolean success = mileageAccountService.deductMileage(member.getId(), amount);
        if (success) {
            mileageHistoryService.logHistory(member, -amount, "차감", "결제 사용");
        }
        return "redirect:/mileage/history"; // 결제 후 내역 페이지로 리다이렉트
    }

    /*// 송금 기능
    @PostMapping("/transfer")
    public String transferMileage(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam int amount, Model model) {
        boolean transferSuccess = mileageAccountService.transferMileage(senderId, receiverId, amount);

        if (transferSuccess) {
            model.addAttribute("message", "송금이 성공적으로 완료되었습니다.");
        } else {
            model.addAttribute("message", "마일리지 잔액이 부족하여 송금에 실패했습니다.");
        }

        return "transferResult";  // 송금 결과 페이지로 리디렉션
    }
*/
}

