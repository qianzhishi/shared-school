package com.sharedschool.backend.controller;

import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CommentService;
import com.sharedschool.backend.typehandler.CommentInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class CommentController {
    @Resource
    private CommentService commentService;

    @GetMapping("/frontend/user/commentList")
    public ApiResponse<List<CommentInfo>> getComment(
            //进行查看评论的用户id
            @RequestParam Long visitorId,
            //被查看的用户id
            @RequestParam Long visitedId,
            //评论类型，1-一级评论，2-二级评论
            @RequestParam int type

    ) {
        return ApiResponse.success(commentService.getCommentList(visitorId, visitedId,type));
    }

    @PostMapping("/frontend/content/comment")
    public ApiResponse<Void> addComment(
            @RequestParam Long userId,
            @RequestParam int type,
            @RequestParam Long fatherId,
            @RequestParam String content
    ) {
        //type: 1-父级id为帖子id，2-父级id为评论id
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setType(type);
            comment.setFatherId(fatherId);
            comment.setContent(content);
            comment.setDate(new Date());
            commentService.save(comment);
            return ApiResponse.success();
    }
}
