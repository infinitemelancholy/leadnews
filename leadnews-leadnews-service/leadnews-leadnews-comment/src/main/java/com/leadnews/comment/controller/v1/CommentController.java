package com.leadnews.comment.controller.v1;


import com.leadnews.comment.service.CommentService;
import com.leadnews.model.comment.dtos.CommentDto;
import com.leadnews.model.comment.dtos.CommentLikeDto;
import com.leadnews.model.comment.dtos.CommentSaveDto;
import com.leadnews.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto){
        return commentService.saveComment(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }

    @PostMapping("/load")
    public ResponseResult findByArticleId(@RequestBody CommentDto dto){
        return commentService.findByArticleId(dto);
    }
}

