package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.CardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    @Resource
    private CardService cardService;

    @GetMapping("/backend/upload/list")
    public ApiResponse<PageInfo<Card>> getCardList(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam int type) {
        PageHelper.startPage(page, size);
        //type: 0代表帖子全部类型 1学习 2娱乐 3表白
        QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
        if (type == 0) {
            List<Card>cardList = cardService.list();
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            return ApiResponse.success(pageInfo);
        }
        else if (type == 1) {
            queryWrapper.eq("card_type", 1);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            return ApiResponse.success(pageInfo);
        }
        else if (type == 2) {
            queryWrapper.eq("card_type", 2);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            return ApiResponse.success(pageInfo);
        }
        else if (type == 3) {
            queryWrapper.eq("card_type", 3);
            List<Card>cardList = cardService.list(queryWrapper);
            PageInfo<Card> pageInfo = new PageInfo<>(cardList);
            return ApiResponse.success(pageInfo);
        }
        return ApiResponse.fail();
    }

}