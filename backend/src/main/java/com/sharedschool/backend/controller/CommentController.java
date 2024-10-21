package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CardService;
import com.sharedschool.backend.service.CommentService;
import com.sharedschool.backend.typehandler.CommentInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private CardService cardService;

    // 获取用户的（一级或二级）评论列表
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

    // 获取帖子或评论的评论列表
    @GetMapping("/frontend/content/commentList")
    public ApiResponse<List<CommentInfo>> getCommentByFatherId(
            @RequestParam Long visitorId,
            @RequestParam Long fatherId,
            @RequestParam int type
    ) {
        return ApiResponse.success(commentService.getCommentListByFatherId(visitorId,fatherId,type));
    }


    // 对帖子或评论进行评论
    @PostMapping("/frontend/content/comment")
    public ApiResponse<Void> addComment(
            @RequestParam Long userId,
            @RequestParam int type,
            @RequestParam Long fatherId,
            @RequestParam String content
    ) {
        //type: 1-父级id为帖子id，2-父级id为评论id
        if (type == 1) {
            Card card = cardService.getById(fatherId);
            card.setCommentNum(card.getCommentNum() + 1);
            cardService.updateById(card);
        }
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setType(type);
            comment.setFatherId(fatherId);
            comment.setContent(content);
            comment.setDate(new Date());
            commentService.save(comment);
            return ApiResponse.success();
    }

    // 删除评论（若为一级评论那么相应的子评论也要删除）
    @PostMapping("/frontend/content/delComment")
    public ApiResponse<Void> delComment(
            @RequestParam Long commentId
    ) {
            if (commentService.getById(commentId).getType() == 1) {

                Long cardId = commentService.getById(commentId).getFatherId();
                Card card = cardService.getById(cardId);
                card.setCommentNum(card.getCommentNum() - 1);
                cardService.updateById(card);

                commentService.removeById(commentId);
                QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("father_id", commentId).eq("comment_type", 2);
                commentService.remove(queryWrapper);
            } else {
                commentService.removeById(commentId);
            }
            return ApiResponse.success();
    }
}
