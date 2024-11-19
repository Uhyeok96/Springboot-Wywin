package com.wywin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)  // 추가: 알려지지 않은 필드 무시
public class ExchangeRateResponseDTO {

    private String result;  // API 응답 상태 (예: "success")

    /*private Map<String, BigDecimal> conversionRates;  // 환율 정보 (USD -> KRW, JPY -> KRW 등)*/

    // Map<String, Double>로 수정: 환율 정보 (USD -> KRW, JPY -> KRW 등)
    @JsonProperty("conversion_rates")  // JSON 필드와 매핑
    private Map<String, Double> conversionRates;

    // 객체를 잘 출력하기 위한 toString() 오버라이드
    @Override
    public String toString() {
        return "ExchangeRateResponseDTO{" +
                "result='" + result + '\'' +
                ", conversionRates=" + conversionRates +
                '}';
    }
}
