package com.wywin.controller;

import com.wywin.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guides")
public class GuideRestController {

    private  CalculatorService calculatorService ;

    @Autowired
    public GuideRestController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /**
     * 항공 운송비용을 계산하는 API 엔드포인트.
     *
     * @param distance      운송 거리 (단위: km)
     * @param weight        화물의 실제 무게 (단위: kg)
     * @param volume        화물의 부피 (단위: m^3)
     * @param ratePerKg     kg당 요금 (단위: USD/kg)
     * @param volumetricFactor 부피에 대한 요금 계산 비율 (일반적으로 166)
     * @param fixedFee      고정 비용 (기본 수수료 등)
     * @return              계산된 운송비용 (단위: USD)
     */

    @PostMapping("/calculator") // POST 요청을 받아 처리하는 메소드
    public double calculatedCost(@RequestParam double distance, // 요청 파라미터로 거리
                                 @RequestParam double weight,   // 요청 파라미터로 무게
                                 @RequestParam double volume,   // 요청 파라미터로 부피
                                 @RequestParam double ratePerKg, // 요청 파라미터로 kg당 요금
                                 @RequestParam double volumetricFactor, // 요청 파라미터로 부피 환산 비율
                                 @RequestParam double fixedFee) { // 요청 파라미터로 고정 비용

        // 서비스 계층의 메소드를 호출하여 실제 운송비용을 계산
        return calculatorService.calculateCost(distance, weight, volume, ratePerKg, volumetricFactor, fixedFee);
    }


}
