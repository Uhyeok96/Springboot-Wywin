package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SellingItem_img")
@Getter
@Setter
@ToString
public class SellingItemImg extends BaseEntity{

    @Id
    @Column(name = "SellingItem_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sid;

    private String simgName; // 이미지 파일명

    private String soriImgName;  // 원본 이미지 파일명

    private String simgUrl;  // 이미지 조회 경로

    private String srepimgYn;    // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)  // 상품 엔티티와 다대일 단방향 관계로 매핑 (지연 로딩 설정으로 매핑된 상품 엔티티 정보가 필요할 경우 데이터 조회)
    @JoinColumn(name = "selling_item_id")
    private SellingItem sellingItem;
    public void updateItemImg(String soriImgName, String simgName, String simgUrl){    // 파라미터로 입력받아서 이미지 정보를 업데이트 하는 메서드
        this.soriImgName = soriImgName;
        this.simgName = simgName;
        this.simgUrl = simgUrl;

    }


}
