package com.wywin.entity;

import com.wywin.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    // 회원이 주문 한 경우에 처리되는 엔티티

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑.

    private LocalDateTime orderDate;    // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){ // 주문상품정보를 담음
        orderItems.add(orderItem);
        orderItem.setOrder(this); // order 엔티티와 orderItem 엔티티가 양방향 참조관계이므로, orderItem 객체에 order 객체를 세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원정보
        for(OrderItem orderItem : orderItemList){ // 상품페이지에서는 하나만 주문하지만 장바구니 페이지에서는 여러개 주문 가능
            order.addOrderItem(orderItem);  // 여러개의 상품을 담을 수 있도록 리스트 형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가
        }
        order.setOrderStatus(OrderStatus.ORDER); // 주문상태를 order로 세팅
        order.setOrderDate(LocalDateTime.now()); // 현재시간을 주문시간으로 세팅
        return order;
    }

    public int getTotalPrice(){ // 총 주문 금액을 구하는 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){ // 주문 취소하는 메서드
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }



}
