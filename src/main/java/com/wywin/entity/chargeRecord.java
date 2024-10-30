package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
public class chargeRecord {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 결제 코드

    private String email; // 사용자 계정
    
    private double amount; // 충전 금액
    
    private LocalDateTime chargeDate;   // 충전 날짜
}
