package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity{

    // 상품 이미지 처리용

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName;  // 원본 이미지 파일명

    private String imgUrl;  // 이미지 조회 경로

    private String repimgYn;    // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)  // 상품 엔티티와 다대일 단방향 관계로 매핑 (지연 로딩 설정으로 매핑된 상품 엔티티 정보가 필요할 경우 데이터 조회)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){    // 파라미터로 입력받아서 이미지 정보를 업데이트 하는 메서드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;

    }


}
