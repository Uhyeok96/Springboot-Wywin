package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ItemLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sellingItem_id")
    private SellingItem sellingItem;
    // 좋아요를 누른 상품

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    // 좋아요를 누른 사용자

    // 좋아요 생성일
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ItemLike(Long sellingItemId, Long memberId) {
    }

    // 생성일 초기화
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // 생성자
    public ItemLike(SellingItem sellingItem, Member member) {
        this.sellingItem = sellingItem;
        this.member = member;
    }


}
