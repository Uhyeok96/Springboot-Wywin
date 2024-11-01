package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    @Id // pk 설정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동번호 생성
    private Long id;    // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;  // 상품명

    @Column(name = "price", nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;  // 상품 판매 상태

  /*  // 상품 업데이트
    public void updateItem(ItemFormDto itemFormDto){
        // 엔티티 클래스에 비즈니스 로직을 추가하면 조금 더 객체지향적으로 코딩할 수 있고, 코드 재활용 가능
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){  // exception.OutOfStockException 클래스 활용
        int restStock = this.stockNumber - stockNumber;  // 재고 수량에서 주문 후 남은 재고 수량을 구함

        if(restStock<0){        // 재고가 주문 수량보다 작을 경우 재고 부족 예외 발생
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;  // 주문 후 재고 수량을 상품의 현재 재고값으로 할당.
    }

    public void addStock(int stockNumber){ //주문 취소 기능 321

        this.stockNumber += stockNumber;
        // 주문을 취소할 경우 주문 수량 만큼 상품 재고를 증가시키는 메서드
    }*/





}
