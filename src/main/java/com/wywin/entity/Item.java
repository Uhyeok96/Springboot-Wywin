package com.wywin.entity;

import com.wywin.constant.ItemStatus;
import com.wywin.dto.ItemFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    //  구매대행 게시판

    @Id // pk 설정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동번호 생성
    private Long id;    // 게시글 고유번호

    @Column(nullable = false, length = 50)
    private String title; // 구매대행 게시글

    @Column(nullable = false, length = 50)
    private String itemNm;  // 상품명

    private String email; // 작성자

    @Column(nullable = false, length = 100)
    private String content; // 게시글 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus itemStatus;  // 상품 판매 상태

//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>(); // 댓글 목록

    public void updateItem(ItemFormDTO itemFormDTO) {
        this.title = itemFormDTO.getTitle();
        this.itemNm = itemFormDTO.getItemNm();
        this.content = itemFormDTO.getContent();
    } // 게시물 수정


}
