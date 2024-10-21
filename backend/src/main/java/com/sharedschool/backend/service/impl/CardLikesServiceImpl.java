package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.CardLikes;
import com.sharedschool.backend.mapper.CardLikesMapper;
import com.sharedschool.backend.service.CardLikesService;
import org.springframework.stereotype.Service;

@Service
public class CardLikesServiceImpl extends ServiceImpl<CardLikesMapper, CardLikes> implements CardLikesService {
    @Override
    public int getLikeFlag(Long cardId, Long userId) {
        return baseMapper.getLikeFlag(cardId, userId);
    }
}
