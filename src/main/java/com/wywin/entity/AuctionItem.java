package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="autionItem")
@Getter
@Setter
@ToString
public class AuctionItem extends BaseEntity{

    @Id // pk 설정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동번호 생성
    private Long id;    // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemName;  // 상품명

    @Column(nullable = false, length = 500)
    private String itemDetail;  // 상품 설명

    @Column(nullable = false)
    private int deposit;  // 보증금

    @Column(nullable = false)
    private int commission;  // 수수료

    @Column(nullable = false)
    private int penalty;  // 벌금

    @Column(nullable = false)
    private int finalPrice; // 최종 낙찰가

    @Column(nullable = false)
    private int bidPrice;  // 경매 시작금액

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;  // 상품 판매 상태

}
