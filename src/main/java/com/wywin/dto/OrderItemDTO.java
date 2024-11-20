package com.wywin.dto;

import com.wywin.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    // 판매 주문조회 (상품 배송, 반품, 교환 등...)

    private String itemNm; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로

    public OrderItemDTO(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getSellingItem().getSitemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    } // 셍성자

}
