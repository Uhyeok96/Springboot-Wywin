package com.wywin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionItemDTO {

    private Long id;    // 상품 코드

    private String itemName;    // 상품명

    private String itemShortDetail;  // 간단한 상품 설명

    private String itemLongDetail;  // 자세한 상품 설명

    private int deposit;  // 보증금

    private int commission;  // 수수료

    private int penalty;  // 벌금

    private int finalPrice; // 최종 낙찰가

    private int bidPrice;   // 경매 시작금액

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private List<AuctionImgDTO> auctionImgs; // 이미지 리스트

}
