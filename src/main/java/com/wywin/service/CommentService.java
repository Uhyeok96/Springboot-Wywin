package com.wywin.service;

import com.wywin.dto.CommentDTO;
import com.wywin.entity.Comment;
import com.wywin.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CommentService {

    private final CommentRepository commentRepository;


    public  CommentDTO saveComment(CommentDTO commentDTO){
        // DTO -> Entity 변환
       /* Comment comment = new Comment();
        comment.setItemId(comment.getItemId());
        comment.setItemNm(commentDTO.getItemNm());
        comment.setPrice(commentDTO.getPrice());
        comment.setContent(commentDTO.getContent());
        comment.setImageUrl(commentDTO.getImageUrl());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setWriter(comment.getWriter());*/

        Comment comment = commentDTO.toEntity();

        // 엔티티 저장
        Comment savedComment = commentRepository.save(comment);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return new CommentDTO(savedComment);
    }


    public List<CommentDTO> getCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedAtDesc(itemId);  // Repository에서 필터링
        log.info(comments);
        return comments.stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
}
