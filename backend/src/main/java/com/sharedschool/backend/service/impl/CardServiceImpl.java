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
}
