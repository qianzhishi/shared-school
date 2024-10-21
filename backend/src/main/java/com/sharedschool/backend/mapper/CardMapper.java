package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.Card;
import com.sharedschool.backend.typehandler.CardInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CardMapper extends BaseMapper<Card> {
    @Select({
            "SELECT "+
            "c.*, "+
            "u.user_name, "+
            "u.user_avatar, "+
            "(IF(cl.card_id IS NOT NULL, 1, 0)) AS likeFlag "+
            "FROM card c "+
            "LEFT JOIN users u ON c.user_id = u.user_id "+
            "LEFT JOIN card_likes cl ON c.card_id = cl.card_id AND cl.user_id = #{userId} "+
            "WHERE c.card_type = #{type}"
    })
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
            @Result(column = "user_name", property = "name"),
            @Result(column = "user_avatar", property = "avatar"),
            @Result(column = "likeFlag", property = "likeFlag"),
    })
    List<CardInfo> selectCardList(@Param("userId") Long userId,@Param("type") int type);

    @Select({
            "SELECT "+
            "c.*, "+
            "u.user_name, "+
            "u.user_avatar, "+
            "(IF(cl.card_id IS NOT NULL, 1, 0)) AS likeFlag "+
            "FROM card c "+
            "LEFT JOIN users u ON c.user_id = u.user_id "+
            "LEFT JOIN card_likes cl ON c.card_id = cl.card_id AND cl.user_id = #{userId} "+
            "WHERE (#{type} = 0 OR c.card_type = #{type}) "+
            "AND c.card_title LIKE CONCAT('%', #{key}, '%') "+
            "ORDER BY c.card_like_number DESC "
    })
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
            @Result(column = "user_name", property = "name"),
            @Result(column = "user_avatar", property = "avatar"),
            @Result(column = "likeFlag", property = "likeFlag"),
    })
    List<CardInfo> searchCardList(@Param("userId") Long userId, @Param("key") String key, @Param("type") int type);

    @Select({
            "SELECT "+
            "c.*, "+
            "u.user_name,"+
            "u.user_avatar, "+
            "(IF(cl.card_id IS NOT NULL, 1, 0)) AS likeFlag "+
            "FROM card c "+
            "LEFT JOIN users u ON c.user_id = u.user_id "+
            "LEFT JOIN card_likes cl ON c.card_id = cl.card_id AND cl.user_id = #{userId} "+
            "ORDER BY c.card_like_number DESC "+
            "LIMIT #{limit}"
    })
    @Results({
            @Result(column = "card_id", property = "cardId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "card_type", property = "type"),
            @Result(column = "card_title", property = "title"),
            @Result(column = "card_content", property = "content"),
            @Result(column = "card_images", property = "images"),
            @Result(column = "card_date", property = "time"),
            @Result(column = "card_view_number", property = "viewNum"),
            @Result(column = "card_like_number", property = "likeNum"),
            @Result(column = "card_comment_number", property = "commentNum"),
            @Result(column = "user_name", property = "name"),
            @Result(column = "user_avatar", property = "avatar"),
            @Result(column = "likeFlag", property = "likeFlag"),
    })
    List<CardInfo> recommendCardList(@Param("userId") Long userId, @Param("limit") int limit);

    @Select(
            "SELECT c.*, u.user_name, u.user_avatar, "+
            "(IF(cl.card_id IS NOT NULL, 1, 0)) AS likeFlag "+
            "FROM card c " +
            "JOIN users u ON c.user_id = u.user_id " +
            "LEFT JOIN card_likes cl ON c.card_id = cl.card_id AND cl.user_id = #{userId} "+
            "WHERE c.user_id IN ( SELECT follower_id FROM user_follows WHERE following_id = #{userId} )" +
            "ORDER BY c.card_date DESC")
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
            @Result(property = "name", column = "user_name"),
            @Result(property = "avatar", column = "user_avatar"),
            @Result(column = "likeFlag", property = "likeFlag"),
    })
    List<CardInfo> selectFollowedCards(@Param("userId") Long userId);

}
