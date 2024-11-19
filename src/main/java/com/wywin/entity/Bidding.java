package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="bidding")
@Getter
@Setter
@ToString
public class Bidding extends BaseEntity{
    // 입찰 기록을 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 입찰 번호 (ID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_item_id", nullable = false)
    private AuctionItem auctionItem;  // 해당 경매 아이템 (AuctionItem과의 관계)

    @Column(nullable = false)
    private String currentBidder;   // 현재 입찰자 이메일

    private String previousBidder;  // 이전 입찰자 이메일 (첫 입찰 시 null)

    @Column(nullable = false)
    private Integer deposit = 0;    // 보증금 (입찰 시 지불한 보증금)

    @Column(nullable = false)
    private Integer biddingPrice;   // 입찰 금액 (입찰 시 기록되는 금액)

}
