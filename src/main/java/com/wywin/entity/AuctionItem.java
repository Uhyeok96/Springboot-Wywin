package com.wywin.entity;

import com.wywin.constant.CurrencyType;
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

    @Column(nullable = false, length = 20, unique = true)
    private String itemName;  // 상품명

    @Column(nullable = false, length = 20)
    private String itemShortDetail;  // 간단한 상품 설명

    @Column(nullable = false, length = 2000)
    private String itemLongDetail;  // 자세한 상품 설명

    @Column(nullable = false)
    private Integer bidPrice;  // 경매 시작금액

    @Column(nullable = false)
    private Integer commission = 0;  // 수수료

    @Column(nullable = false)
    private Integer penalty = 0;  // 벌금

    @Column(nullable = false)
    private Integer finalPrice = 0; // 최종 낙찰가

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus = ItemStatus.ONBID; // 상품 판매 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType = CurrencyType.KRW; // 경매 상품 화폐 종류 (기본값: KRW)

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AuctionImg> auctionImgs; // 연관된 이미지 리스트

    @Column(nullable = false)
    private Integer auctionPeriod;  // 경매 기간 (1, 3, 5, 7)

    @Column(nullable = false)
    private LocalDateTime auctionEndDate;  // 경매 종료 일시

    // 예상 견적가 필드 추가
    @Column(nullable = false)
    private Integer estimatedPrice = 0; // 예상 견적가 (기본값은 0)

    @OneToMany(mappedBy = "auctionItem", fetch = FetchType.LAZY)
    private List<Bidding> biddings; // 해당 경매 아이템에 대한 입찰 목록

}
