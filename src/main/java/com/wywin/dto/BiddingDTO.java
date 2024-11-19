package com.wywin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BiddingDTO {

    private Long id;  // 입찰 번호 (ID)

    @NotNull(message = "경매 아이템은 필수입니다.")
    private Long auctionItemId;  // 경매 아이템 ID (AuctionItem 엔티티와 관계)

    @NotBlank(message = "입찰자 이메일은 필수입니다.")
    private String currentBidder;  // 현재 입찰자 이메일

    private String previousBidder;  // 이전 입찰자 이메일 (첫 입찰 시 null일 수 있음)

    @NotNull(message = "보증금은 필수입니다.")
    private Integer deposit;  // 보증금 (입찰 시 지불한 보증금)

    @NotNull(message = "입찰 금액은 필수입니다.")
    private Integer biddingPrice;  // 입찰 금액 (입찰 시 기록되는 금액)
}
