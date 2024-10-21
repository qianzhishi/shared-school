package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CardLikesService;
import com.sharedschool.backend.service.CardService;
import com.sharedschool.backend.service.CommentService;
import com.sharedschool.backend.service.UserService;
import com.sharedschool.backend.typehandler.CardDetailInfo;
import com.sharedschool.backend.typehandler.CardInfo;
import com.sharedschool.backend.typehandler.CommentInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CardController {

    @Resource
    private CardService cardService;

    @Resource
    private CardLikesService likeService;

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    // 后台获取帖子列表
    @GetMapping("/backend/upload/list")
    public ApiResponse<PageInfo<Card>> getCardList(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam int type) {
        PageHelper.startPage(page, size);
        //type: 0-代表帖子全部类型 1-学习 2-娱乐 3-表白
        QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
        if (type == 0) {
            List<Card>cardList = cardService.list();
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            PageHelper.clearPage();
            return ApiResponse.success(pageInfo);
        }
        else if (type == 1) {
            queryWrapper.eq("card_type", 1);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            PageHelper.clearPage();
            return ApiResponse.success(pageInfo);
        }
        else if (type == 2) {
            queryWrapper.eq("card_type", 2);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            PageHelper.clearPage();
            return ApiResponse.success(pageInfo);
        }
        else if (type == 3) {
            queryWrapper.eq("card_type", 3);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            PageHelper.clearPage();
            return ApiResponse.success(pageInfo);
        }
        PageHelper.clearPage();
        return ApiResponse.fail();
    }

    // 主页获取帖子内容列表
    @GetMapping("/frontend/content/list")
    public ApiResponse<PageInfo<CardInfo>> getCardList(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam int type
    ) {
        PageHelper.startPage(page, size);
        List<CardInfo> cardInfoList = cardService.getCardList(userId, type);
        PageInfo<CardInfo> pageInfo = new PageInfo<>(cardInfoList);
        PageHelper.clearPage();
        return ApiResponse.success(pageInfo);

    }

    // 获取用户发布的帖子列表
    @GetMapping("/frontend/user/content")
    public ApiResponse<List<Card>> getUserCardList(@RequestParam Long userId) {
        QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Card> cardList = cardService.list(queryWrapper);
        return ApiResponse.success(cardList);
    }

    // 获取帖子详情
    @GetMapping("/frontend/content/detail")
    public ApiResponse<CardDetailInfo> getCardDetail(@RequestParam Long userId,@RequestParam Long cardId) {
        CardDetailInfo cardDetailInfo = new CardDetailInfo();
        Card card = cardService.getById(cardId);
        card.setViewNum(card.getViewNum() + 1);
        cardService.updateById(card);
        cardDetailInfo.setCardInfo(card);

        User user = userService.getById(card.getUserId());
        cardDetailInfo.setName(user.getUsername());
        cardDetailInfo.setAvatar(user.getAvatar());

        cardDetailInfo.setLikeFlag(likeService.getLikeFlag(cardId, userId));

        List<CommentInfo> commentInfoList = commentService.getCommentListByFatherId(userId,cardId,1);
        cardDetailInfo.setCommentList(commentInfoList);
        return ApiResponse.success(cardDetailInfo);

    }

    // 获取搜索内容
    @GetMapping("/frontend/content/search")
    public ApiResponse<PageInfo<CardInfo>> getSearchCardList(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam int type,
            @RequestParam int page,
            @RequestParam int size
    ) {
        PageHelper.startPage(page, size);
        List<CardInfo> cardInfoList = cardService.searchCardList(userId,keyword,type);
        PageInfo<CardInfo> pageInfo = new PageInfo<>(cardInfoList);
        PageHelper.clearPage();
        return ApiResponse.success(pageInfo);
    }

    // 获取推荐内容
    @GetMapping("/frontend/content/recommend")
    public ApiResponse<List<CardInfo>> getRecommendCardList(
            @RequestParam Long userId
    ) {
        List<CardInfo> cardInfoList = cardService.recommendCardList(userId,10);
        return ApiResponse.success(cardInfoList);
    }

    // 获取用户的关注圈所发布的内容
    @GetMapping("/frontend/content/likeNews")
    public ApiResponse<List<CardInfo>> getFollowCardList(
            @RequestParam Long userId
    ) {
        List<CardInfo> cardInfoList = cardService.getFollowedCards(userId);
        return ApiResponse.success(cardInfoList);
    }

    // 发布帖子
    @PostMapping("/frontend/upload/post")
    public ApiResponse<Void> addCard(
            @RequestParam Long userId,
            @RequestParam int type,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam List<String> images

    ) {
        Card card = new Card();
        card.setUserId(userId);
        card.setType(type);
        card.setTitle(title);
        card.setContent(content);
        Date currentDate = new Date();
        card.setDate(currentDate);
        card.setImages(images);
        cardService.save(card);
        return ApiResponse.success();
    }

    // 修改帖子
    @PostMapping("/frontend/content/editPost")
    public ApiResponse<Void> updateCard(
            @RequestParam Long cardId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam List<String> images
    ) {
        Card card = cardService.getById(cardId);
        card.setTitle(title);
        card.setContent(content);
        card.setImages(images);
        cardService.updateById(card);
        return ApiResponse.success();
    }

    // 删除帖子
    @PostMapping("/frontend/content/delPost")
    public ApiResponse<Void> deleteCard(@RequestParam Long cardId) {
        cardService.removeById(cardId);
        return ApiResponse.success();
    }
}
