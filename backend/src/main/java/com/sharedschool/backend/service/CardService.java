package com.sharedschool.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.typehandler.CardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardService extends IService<Card> {
    List<CardInfo> getCardList(Long userId, int type);
    List<CardInfo> searchCardList(Long userId, String key, int type);
    List<CardInfo> recommendCardList(Long userId, int limit);
    List<CardInfo> getFollowedCards(Long userId);
    List<CardInfo> getCardList(int type);
}
