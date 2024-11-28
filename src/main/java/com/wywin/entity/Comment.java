package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment {
    // 구매대행 글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemNm;

    private int price;

    private String content; // 댓글 내용

    /*private String imageUrl;*/

    private String writer;

    private LocalDateTime createdAt; // 작성일

    @Column(name = "item_id")
    private Long itemId; // 댓글이 속한 아이템의 ID

}
