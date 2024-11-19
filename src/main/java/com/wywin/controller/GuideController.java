package com.wywin.controller;

import com.wywin.service.CalculatorService;
import com.wywin.dto.CalculatedCostDTO;  // DTO 클래스 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guides")
public class GuideController {

    @Autowired
    CalculatorService calculatorService;

    // GET 요청: 계산 폼을 반환 : 사용자가 폼을 요청할 때 GET 요청이 들어옵니다. 이 때 CalculatorForm() 메소드가 호출되어 폼을 반환합니다.
    @GetMapping(value = "/calculator")
    public String CalculatorForm(Model model) {
        model.addAttribute("calculatedCostDTO", new CalculatedCostDTO());
        return "guides/calculator";  // "calculator" HTML 페이지 반환
    }

    // POST 요청: 계산 결과 처리
    @PostMapping(value = "/calculator")
    public String calculateCost(CalculatedCostDTO calculatedCostDTO, Model model) {
        // 서비스 메소드 호출하여 비용 계산
        double totalCost = calculatorService.calculateCost(
                calculatedCostDTO.getDistance(),
                calculatedCostDTO.getWeight(),
                calculatedCostDTO.getVolume(),
                calculatedCostDTO.getRatePerKg(),
                calculatedCostDTO.getVolumetricFactor(),
                calculatedCostDTO.getFixedFee()
        );/*사용자가 폼을 제출하면 POST 요청으로 전달된 데이터를 CalculatedCostDTO 객체에 바인딩하고,
        calculateCost() 메소드에서 계산을 처리합니다.
        CalculatedCostDTO 폼 데이터를 담고 있는 DTO 클래스입니다.
        */

        // 결과를 모델에 추가
        model.addAttribute("calculatedCost", totalCost);

        // 계산된 결과를 "calculator" 페이지에 전달
        return "guides/calculator";
    }/*계산된 결과는 model.addAttribute()로 뷰에 전달되어,
    guides/calculator 템플릿에서 결과를 출력할 수 있습니다.*/

    @GetMapping(value = "/usageCharge")
    public String usageChargeForm() {

        return "guides/usageCharge";
    }

    @GetMapping(value = "/auction")
    public String auctionForm() {

        return "guides/auction";
    }

    @GetMapping(value = "/substitutionForPurchasing")
    public String substitutionForPurchasingForm() {

        return "guides/substitutionForPurchasing";
    }

    @GetMapping(value = "/refunCancelReturn")
    public String refunCancelReturnForm() {

        return "guides/refunCancelReturn";
    }

    @GetMapping(value = "/damageLostRecover")
    public String damageLostRecoverForm() {

        return "guides/damageLostRecover";
    }

    @GetMapping(value = "/delivery")
    public String deliveryForm() {

        return "guides/delivery";
    }

    @GetMapping(value = "/internationalShippingFee")
    public String internationalShippingFeeForm() {

        return "guides/internationalShippingFee";
    }

    @GetMapping(value = "/customsClearance")
    public String customsClearanceForm() {

        return "guides/customsClearance";
    }

    @GetMapping(value = "/dutyTaxrate")
    public String dutyTaxrateForm() {

        return "guides/dutyTaxrate";
    }

    @GetMapping(value = "/bankbookDepositWithdrawal")
    public String bankbookDepositWithdrawalForm() {

        return "guides/bankbookDepositWithdrawal";
    }

}
