package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 장바구니에는 여러 개의 상품을 담을 수 있으므로 다대일 관계 매핑
    @JoinColumn(name = "cart_id")   // foreign key (cart_id) references cart(card_id)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 상품은 여러 장바구니에 담길 수 있으므로 다대일 관계 매핑
    @JoinColumn(name = "sellingItem_id")
    private SellingItem sellingItem;

    private int count;  // 같은 상품을 장바구니에 몇 개 담을지 저장

    public static CartItem createCartItem(Cart cart, SellingItem sellingItem, int count) {  // 331 추가 장바구니용
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setSellingItem(sellingItem);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){

        this.count += count;
    }  // 장바구니에 기존 담겨 있는 상품인데, 해당 상품을 추가한경우 누적

    // 장바구니에 담겨있는 상품의 수량 변경
    public void updateCount(int count){  // 351 추가
        this.count = count;
    }

}

