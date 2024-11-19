package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class ExchangeRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동으로 ID가 증가하도록 설정
    private Long id;  // 고정된 id 값

    // USD를 기준으로 한 원화 -> 원화 환율 (1달러에 대해 몇 원화인지)
    @Column(nullable = false)
    private BigDecimal usdToKrw;

    // USD를 기준으로 한 엔화 -> 엔화 환율 (1달러에 대해 몇 엔화인지)
    @Column(nullable = false)
    private BigDecimal usdToJpy; // 필드명 변경: 1달러에 대한 엔화 환율
}
