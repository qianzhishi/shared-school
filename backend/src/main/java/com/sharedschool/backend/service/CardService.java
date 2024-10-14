package com.sharedschool.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.typehandler.CardInfo;

import java.util.List;

public interface CardService extends IService<Card> {
    List<CardInfo> getCardList(Long userId,int type);
}
