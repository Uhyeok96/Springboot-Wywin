package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mileage_account_id")
    private MileageAccount mileageAccount;  // 트랜잭션과 연결된 마일리지 계좌

    private String type;  // "충전", "결제", "출금"
    private BigDecimal amount;  // 거래 금액
    private LocalDateTime transactionDate;  // 거래 날짜
}
