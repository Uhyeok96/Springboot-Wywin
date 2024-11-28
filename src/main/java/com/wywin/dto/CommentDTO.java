package com.wywin.dto;

import com.wywin.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;

    private String itemNm;

    private int price;

    private String content;

    private String writer;

    private Long itemId;

    private LocalDateTime createdAt; // 작성일

    // 모든 필드를 포함하는 생성자 작성
    public CommentDTO(Long id, String itemNm, int price, String content, LocalDateTime createdAt) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Comment 엔티티를 DTO로 변환하는 생성자
    public CommentDTO(Comment comment) {
        this.itemId = comment.getItemId();
        this.itemNm = comment.getItemNm();
        this.price = comment.getPrice();
        this.content = comment.getContent();
        /*this.imageUrl = comment.getImageUrl();*/
        this.writer = comment.getWriter();
        this.createdAt = comment.getCreatedAt();
    }

    // DTO를 엔티티로 변환하는 메소드 추가
    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setItemNm(this.itemNm);
        comment.setPrice(this.price);
        comment.setContent(this.content);
        comment.setWriter(this.writer);
        comment.setItemId(this.itemId);  // itemId가 Comment 엔티티에 설정됩니다.
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }
}
