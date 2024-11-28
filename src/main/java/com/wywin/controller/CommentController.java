package com.wywin.controller;

import com.wywin.dto.CommentDTO;
import com.wywin.entity.Comment;
import com.wywin.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/save")
    @ResponseBody
    public CommentDTO saveComment(@RequestBody CommentDTO commentDTO, Principal principal){
        System.out.println("Received DTO: " + commentDTO); // 디버깅용 출력
        System.out.println("Content: " + commentDTO.getContent());
        System.out.println("ItemId: " + commentDTO.getItemId());
        System.out.println("생성 시간: " + commentDTO.getCreatedAt());

        commentDTO.setWriter(principal.getName());
        // DTO를 서비스로 전달하여 저장한 후, 저장된 DTO를 반환
        CommentDTO savedComment = commentService.saveComment(commentDTO);
        return savedComment;
    }

}
