package com.wywin.dto;

import com.wywin.entity.ExchangeRate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExchangeRateDTO {

    private Long id;

    private BigDecimal usdToKrw;    // 달러기준 원화환율

    private BigDecimal usdToJpy;    // 달러기준 엔화환율

    private LocalDateTime regTime;  // 생성 시간

    private LocalDateTime updateTime;  // 수정 시간

    // 생성자
    public ExchangeRateDTO(Long id, BigDecimal usdToKrw, BigDecimal usdToJpy, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.usdToKrw = usdToKrw;
        this.usdToJpy = usdToJpy;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

    // Entity -> DTO 변환
    public static ExchangeRateDTO fromEntity(ExchangeRate exchangeRate) {
        return new ExchangeRateDTO(
                exchangeRate.getId(),
                exchangeRate.getUsdToKrw(),
                exchangeRate.getUsdToJpy(),
                exchangeRate.getRegTime(),
                exchangeRate.getUpdateTime()
        );
    }

    // DTO -> Entity 변환
    public ExchangeRate toEntity() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setUsdToKrw(this.usdToKrw);
        exchangeRate.setUsdToJpy(this.usdToJpy);
        return exchangeRate;
    }

}
