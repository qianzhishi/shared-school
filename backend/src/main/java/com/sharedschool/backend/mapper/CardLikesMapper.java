package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.CardLikes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CardLikesMapper extends BaseMapper<CardLikes> {
    @Select("SELECT IF(COUNT(*) > 0, 1, 0) AS likeFlag FROM card_likes WHERE card_id = #{cardId} AND user_id = #{userId}")
    int getLikeFlag(@Param("cardId") Long cardId, @Param("userId") Long userId);
}
