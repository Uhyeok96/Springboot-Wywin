package com.wywin.repository;

import com.wywin.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 itemId로 댓글 조회
    List<Comment> findByItemIdOrderByCreatedAtDesc(Long itemId);
}
