package com.wywin.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    /*
     * 항공 운송비용을 계산하는 메소드.

      @param distance      운송 거리 (단위: km)
      @param weight        화물의 실제 무게 (단위: kg)
      @param volume        화물의 부피 (단위: m^3)
      @param ratePerKg     kg당 요금 (단위: USD/kg)
      @param volumetricFactor 부피에 대한 요금 계산 비율 (일반적으로 166)
      @param fixedFee      고정 비용 (기본 수수료 등)
      @return              계산된 운송비용 (단위: USD)
     */

    public double calculateCost(double distance, double weight, double volume,
                                double ratePerKg, double volumetricFactor, double fixedFee) {

        // 부피 무게 계산 (부피 무게는 부피와 volumetricFactor를 이용하여 계산) 미국 파운드, 166. 한국 kg,5000
        double volumetricWeight = volume / volumetricFactor;

        // 실제 무게와 부피 무게 중 더 큰 값을 선택
        double chargeableWeight = Math.max(weight, volumetricWeight);

        // 거리별 기본 운송비용 (단위: USD)
        double distanceCost = distance * 0.05; // 예시로 km당 0.05 USD 비용

        // 무게당 비용 계산
        double weightCost = chargeableWeight * ratePerKg;

        // 총 비용 계산
        double totalCost = distanceCost + weightCost + fixedFee;

        return totalCost;
    }

}
