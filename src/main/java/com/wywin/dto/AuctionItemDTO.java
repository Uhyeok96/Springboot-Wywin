package com.wywin.dto;

import com.wywin.constant.CurrencyType;
import com.wywin.constant.ItemStatus;
import com.wywin.entity.AuctionItem;
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

    private Integer commission;  // 수수료

    private Integer penalty;  // 벌금

    private Integer finalPrice; // 최종 낙찰가

    @NotNull(message = "경매 기간을 선택해주세요.")
    private Integer auctionPeriod;  // 경매 기간 (1일, 3일, 5일, 7일 중 선택)

    private LocalDateTime auctionEndDate;   // 경매 종료 일시

    private LocalDateTime regTime;  // 상품 등록시간

    private LocalDateTime updateTime;   // 상품 수정시간

    private List<AuctionImgDTO> auctionImgs; // 이미지 리스트

    private String createdBy; // 등록자

    private String modifiedBy; // 수정자

    @NotNull(message = "화폐 종류는 필수입니다.")
    private CurrencyType currencyType = CurrencyType.KRW; // 화폐 종류 (기본값은 KRW)

    @NotNull(message = "상품 상태는 필수입니다.")
    private ItemStatus itemStatus = ItemStatus.ONBID; // 경매 상품 상태 (기본값은 ONBID)

    // 예상 견적가 필드 추가
    private Integer estimatedPrice = 0; // 예상 견적가

    private Integer biddingCount; // 입찰 수

    // 추가된 필드: 마지막 입찰 정보
    private BiddingDTO lastBiddingDTO; // 최신 입찰 정보
    private BiddingDTO userLastBiddingDTO; // 해당 회원의 마지막 입찰 정보
}
