package com.wywin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartOrderDTO {
    // 장바구니 상품 주문하기

    private Long cartItemId;

    private List<CartOrderDTO> cartOrderDTOList; // 장바구니에서 여러개의 상품을 주문함
}
