package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="auction_item")
@Getter
@Setter
@ToString
public class AuctionItem extends BaseEntity{

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 상품 코드

    @Column(nullable = false, length = 20)
    private String itemName;  // 상품명

    @Column(nullable = false, length = 20)
    private String itemShortDetail;  // 간단한 상품 설명

    @Column(nullable = false, length = 2000)
    private String itemLongDetail;  // 자세한 상품 설명

    @Column(nullable = true) // 보증금은 null 가능
    private Integer deposit;  // 보증금

    @Column(nullable = true) // 수수료는 null 가능
    private Integer commission;  // 수수료

    @Column(nullable = true) // 벌금은 null 가능
    private Integer penalty;  // 벌금

    @Column(nullable = true) // 최종 낙찰가는 null 가능
    private Integer finalPrice; // 최종 낙찰가

    @Column(nullable = false)
    private Integer bidPrice;  // 경매 시작금액

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus = ItemStatus.ONBID; // 상품 판매 상태

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AuctionImg> auctionImgs; // 연관된 이미지 리스트

    @Column(nullable = false)
    private Integer auctionPeriod;  // 경매 기간 (1, 3, 5, 7)

    @Column(nullable = false)
    private LocalDateTime auctionEndDate;  // 경매 종료 일시

}
