package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.typehandler.CardInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CardMapper extends BaseMapper<Card> {
    @Select("SELECT * FROM card WHERE card_type = #{type}")
    @Results({
            @Result(column = "card_id", property = "cardId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "card_type", property = "type"),
            @Result(column = "card_title", property = "title"),
            @Result(column = "card_content", property = "content"),
            @Result(column = "card_date", property = "time"),
            @Result(column = "card_view_number", property = "viewNum"),
            @Result(column = "card_like_number", property = "likeNum"),
            @Result(column = "card_comment_number", property = "commentNum"),
            @Result(property = "name", column = "user_id",
                    one = @One(select = "com.sharedschool.backend.mapper.UserMapper.selectName")),
            @Result(property = "avatar", column = "user_id",
                    one = @One(select = "com.sharedschool.backend.mapper.UserMapper.selectAvatar")),
            @Result(property = "likeFlag", column = "card_id",
                    one = @One(select = "com.sharedschool.backend.mapper.CardLikesMapper.selectLikeFlag"))
    })
    List<CardInfo> selectCardList(@Param("userId") Long userId,@Param("type") int type);
}
