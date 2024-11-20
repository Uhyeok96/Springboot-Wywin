package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   // 1:1 매핑 (회원과 카트)
    @JoinColumn(name = "member_id") // 조인은 회원의 id와(매핑할 외래키 foreign key(member_id) references member )
    // name을 명시하지 않으면 JPA가 알아서 ID를 찾지만 컬럼명이 원하는 대로 생성되지 않을 수 있다.
    private Member member;

    public static Cart createCart(Member member){  // 장바구니에 회원정보 넣어서 장바구니에 보내는 호직
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
