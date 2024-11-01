package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="sellingItem")
@Getter
@Setter
@ToString
public class SellingItem extends BaseEntity{

    @Id
    @Column(name="sellingItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid; // 판매코드

    @Column(nullable = false, length = 50)
    private String sitemNm; // 판매 아이템 이름

    @Column(name = "price", nullable = false)
    private int sprice; // 판매 가격

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Column(nullable = false)
    private int stockNumbers;   // 재고수량

    private String seller; // 판매자

    private String buyer; // 구매자

    private int quantity; // 구매 수량

    private LocalDateTime sellingDate; // 판매일

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus; // 판매 상태
}
