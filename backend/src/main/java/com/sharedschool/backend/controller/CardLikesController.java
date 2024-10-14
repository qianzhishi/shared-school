package com.sharedschool.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.entity.CardLikes;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CardLikesService;
import com.sharedschool.backend.service.CardService;
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

            Card card = cardService.getById(cardId);
            card.setLikeNum(card.getLikeNum() + 1);
            cardService.updateById(card);
            return ApiResponse.success(card.getLikeNum());
        } else {    //取消点赞
            QueryWrapper<CardLikes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("card_id", cardId).eq("user_id", userId);
            cardLikesService.remove(queryWrapper);

            Card card = cardService.getById(cardId);
            card.setLikeNum(card.getLikeNum() - 1);
            cardService.updateById(card);
            return ApiResponse.success(card.getLikeNum());
        }

    }
}
