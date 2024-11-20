package com.wywin.dto;

import com.wywin.constant.OrderStatus;
import com.wywin.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDTO {
    // 주문 정보를 담음 (구매자 정보와 상태)

    private Long orderId; // 주문 아이디

    private String orderDate; // 주문날짜

    private OrderStatus orderStatus; // 주문상테

    public OrderHistDTO(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"));
        this.orderStatus = order.getOrderStatus();

        System.out.println("Order Status: " + order.getOrderStatus());
    }

    // 주문 상품 리스트
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        orderItemDTOList.add(orderItemDTO);
    } // 주문 사움 리스트에 추가하는 메소드


}
