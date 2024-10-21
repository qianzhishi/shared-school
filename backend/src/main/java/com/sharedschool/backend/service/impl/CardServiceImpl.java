package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.mapper.CardMapper;
import com.sharedschool.backend.service.CardService;
import com.sharedschool.backend.typehandler.CardInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {
    @Override
    public List<CardInfo> getCardList(Long userId, int type) {
        return baseMapper.selectCardList(userId,type);
    }

    @Override
    public List<CardInfo> searchCardList(Long userId, String key, int type) {
        return baseMapper.searchCardList(userId,key,type);
    }

    @Override
    public List<CardInfo> recommendCardList(Long userId, int limit) {
        return baseMapper.recommendCardList(userId,limit);
    }

    @Override
    public List<CardInfo> getFollowedCards(Long userId) {
        return baseMapper.selectFollowedCards(userId);
    }

}
