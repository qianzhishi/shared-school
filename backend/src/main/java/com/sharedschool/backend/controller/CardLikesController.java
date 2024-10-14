package com.sharedschool.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.entity.CardLikes;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CardLikesService;
import com.sharedschool.backend.service.CardService;
import com.sharedschool.backend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardLikesController {
    @Resource
    private CardLikesService cardLikesService;

    @Resource
    private CardService cardService;

    @Resource
    private UserService userService;

    @PostMapping("/frontend/content/likePost")
    public ApiResponse<Long> likeCard(
            @RequestParam Long cardId,
            @RequestParam Long userId,
            @RequestParam int state
    ) {
        // state: 0:未点赞 1:已点赞
        if(state == 0) {    //点赞帖子
            CardLikes cardLikes = new CardLikes();
            cardLikes.setCardId(cardId);
            cardLikes.setUserId(userId);
            cardLikesService.save(cardLikes);

            // 获取帖子
            Card card = cardService.getById(cardId);
            // 点赞数加1
            card.setLikeNum(card.getLikeNum() + 1);
            cardService.updateById(card);

            // 获取帖子所属用户
            User user = userService.getById(card.getUserId());
            // 用户点赞数加1
            user.setLikeNum(user.getLikeNum() + 1);
            userService.updateById(user);
            return ApiResponse.success(card.getLikeNum());
        } else {    //取消点赞
            // 构造查询条件
            QueryWrapper<CardLikes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("card_id", cardId).eq("user_id", userId);
            // 删除点赞记录
            cardLikesService.remove(queryWrapper);

            // 获取帖子
            Card card = cardService.getById(cardId);
            // 点赞数减1
            card.setLikeNum(card.getLikeNum() - 1);
            cardService.updateById(card);

            // 获取帖子所属用户
            User user = userService.getById(card.getUserId());
            // 用户点赞数减1
            user.setLikeNum(user.getLikeNum() - 1);
            userService.updateById(user);

            return ApiResponse.success(card.getLikeNum());
        }

    }
}
