package com.wywin.dto;

import com.wywin.constant.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDTO {

    private String searchDateType;              // 현재 시간과 상품 등록일을 비교하여 상품 데이터를 조회함
    /*조회 시간 기준 : all(상품 등록일 전체), 1d(최근 하루 동안 등록된 상품),
            1w(최근 일주일 동안 등록된 상품), 1m(최근 한달 동안 등록된 상품), 6m(최근 6개월 동안 등록된 상품)   */

    private ItemStatus searchSellStatus;    // 판매상태를 기준으로 상품 데이터를 조회

    private String searchBy;                    // 어떤 유형으로 조회할 지 선택 (itemNm:상품명, createdBy:상품 등록자 아이디)

    private String searchQuery = "";            // 조회할 검색어 저장할 변수(SearchBy가 itemNm일 경우 상품명을 기준으로 검색하고,
    // createdBy일 경우 상품 등록자 아이디 기준으로 검색

    // repository.ItemRepositoryCustom.java에서 활용
}
