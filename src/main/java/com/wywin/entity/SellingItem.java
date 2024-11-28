package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="sellingItem")
@Getter
@Setter
@ToString
public class SellingItem extends BaseEntity{

    @Id
    @Column(name="sellingItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid; // 판매코드

    @Column(nullable = false, length = 50)
    private String sitemNm; // 판매 아이템 이름

    @Column(name = "price", nullable = false)
    private int sprice; // 판매 가격

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Column(nullable = false)
    private int stockNumbers;   // 재고수량

    private String seller; // 판매자

    private String buyer; // 구매자

    private int quantity; // 구매 수량

    private LocalDateTime sellingDate; // 판매일

    // 좋아요 수 필드 (실제로 DB에 저장되지 않으며, 조회 시에만 사용)
    @Transient // DB에 저장되지 않도록 Transient로 설정
    private Long likeCount;

    // 현재 사용자가 좋아요를 눌렀는지 여부
    private boolean isLikedByCurrentUser;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus; // 판매 상태

    @OneToMany(mappedBy = "sellingItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellingItemImg> sellingImgs = new ArrayList<>();


    // 상품 업데이트
    public void updateItem(SellingItemFormDTO sellingItemFormDTO){
        // 엔티티 클래스에 비즈니스 로직을 추가하면 조금 더 객체지향적으로 코딩할 수 있고, 코드 재활용 가능
        this.sitemNm = sellingItemFormDTO.getSitemNm();
        this.sprice = sellingItemFormDTO.getSprice();
        this.stockNumbers = sellingItemFormDTO.getStockNumbers();
        this.itemDetail = sellingItemFormDTO.getItemDetail();
        this.itemStatus = sellingItemFormDTO.getItemStatus();
    }

    public void removeStock(int stockNumbers){
        int restStock = this.stockNumbers - stockNumbers; // 상품 재고 수량에서 주문 후 남은 재고 수량을 구함
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : " + this.stockNumbers + ")");
            // 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킴
        }
        this.stockNumbers = restStock; // 주문 후 남은 재고 수량을 상춤의 현재 재고 값으로 할당
    }

    public void addStock(int stockNumbers){ // 주문을 취소할 경우 주문 수량만큼 재고 증가
        this.stockNumbers += stockNumbers;
    }
}
