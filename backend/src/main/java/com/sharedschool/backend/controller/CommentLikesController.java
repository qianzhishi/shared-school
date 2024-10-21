package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.entity.CommentLikes;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CommentLikesService;
import com.sharedschool.backend.service.CommentService;
import com.sharedschool.backend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentLikesController {
    @Resource
    private CommentLikesService commentLikesService;

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    // 点赞/取消点赞评论
    @PostMapping("/frontend/content/likeComment")
    public ApiResponse<Long> likeComment(
            @RequestParam Long commentId,
            @RequestParam Long userId,
            @RequestParam int state
    ) {
        // state: 0-未点赞 1-已点赞
        if(state == 0) {    //点赞评论
            CommentLikes commentLikes = new CommentLikes();
            commentLikes.setCommentId(commentId);
            commentLikes.setUserId(userId);
            commentLikesService.save(commentLikes);

            // 获取评论
            Comment comment = commentService.getById(commentId);
            // 点赞数加1
            comment.setLikeNum(comment.getLikeNum() + 1);
            commentService.updateById(comment);

            // 获取评论的用户
            User user = userService.getById(comment.getUserId());
            // 用户点赞数加1
            user.setLikeNum(user.getLikeNum() + 1);
            userService.updateById(user);

            return ApiResponse.success(comment.getLikeNum());
        } else {    //取消点赞
            // 构造查询条件
            QueryWrapper<CommentLikes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comment_id", commentId).eq("user_id", userId);
            // 删除点赞记录
            commentLikesService.remove(queryWrapper);

            // 获取评论
            Comment comment = commentService.getById(commentId);
            // 点赞数减1
            comment.setLikeNum(comment.getLikeNum() - 1);
            commentService.updateById(comment);

            // 获取评论的用户
            User user = userService.getById(comment.getUserId());
            // 用户点赞数减1
            user.setLikeNum(user.getLikeNum() - 1);
            userService.updateById(user);

            return ApiResponse.success(comment.getLikeNum());
        }

    }

}
