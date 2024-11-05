package com.wywin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionItemDTO {
    // html코드에서도 required 를 사용하여 유효성 검사를 하지만 서버측에서도 어노테이션으로 유효성 검사를 하여 안정성을 확보함.

    private Long id;    // 상품 코드

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 20, message = "상품명은 최대 20자까지 입력 가능합니다.")
    private String itemName;    // 상품명

    @NotBlank(message = "간단한 상품 설명은 필수입니다.")
    @Size(max = 20, message = "간단한 상품 설명은 최대 20자까지 입력 가능합니다.")
    private String itemShortDetail;  // 간단한 상품 설명

    @NotBlank(message = "자세한 상품 설명은 필수입니다.")
    @Size(max = 2000, message = "자세한 상품 설명은 최대 2000자까지 입력 가능합니다.")
    private String itemLongDetail;  // 자세한 상품 설명

    @NotNull(message = "경매 시작금액은 필수입니다.")
    private Integer bidPrice;   // 경매 시작금액

    // 보증금, 수수료, 벌금, 최종 낙찰가는 선택적이므로 @NotNull 제거
    private Integer deposit;  // 보증금

    private Integer commission;  // 수수료

    private Integer penalty;  // 벌금

    private Integer finalPrice; // 최종 낙찰가

    @NotNull(message = "경매 기간을 선택해주세요.")
    private Integer auctionPeriod;  // 경매 기간 (1일, 3일, 5일, 7일 중 선택)

    private LocalDateTime auctionEndDate;   // 경매 종료 일시

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private List<AuctionImgDTO> auctionImgs; // 이미지 리스트

}
