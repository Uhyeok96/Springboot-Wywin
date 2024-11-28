package com.wywin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class MileageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 거래 고유 ID

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // Member와 연관 관계 설정

    private int amount;  // 거래 금액
    private String type;  // 거래 종류 (충전/출금)
    private String description;  // 거래 설명

    private LocalDateTime transactionDate;  // 거래 일시

}
