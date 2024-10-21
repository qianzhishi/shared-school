package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.typehandler.CommentInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT " +
            "c.comment_id, c.user_id, c.comment_content, c.comment_date, c.like_number, c.father_id," +
            "u.user_name, u.user_avatar, " +
            "(IF(cl.comment_like_id IS NOT NULL, 1, 0)) AS likeFlag " +
            "FROM comments c " +
            "LEFT JOIN users u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_likes cl ON c.comment_id = cl.comment_id AND cl.user_id = #{visitorId} " +
            "WHERE c.user_id = #{visitedId} And c.comment_type = #{type}")
    @Results({
            @Result(column = "comment_id", property = "commentId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "comment_content", property = "content"),
            @Result(column = "comment_date", property = "time"),
            @Result(column = "like_number", property = "likeNum"),
            @Result(column = "father_id", property = "fatherId"),
            @Result(column = "user_name", property = "name"),
            @Result(column = "user_avatar", property = "avatar"),
            @Result(column = "likeFlag", property = "likeFlag")
    })
    List<CommentInfo> getCommentList(@Param("visitorId") Long visitorId, @Param("visitedId") Long visitedId,@Param("type") int type);

    @Select("SELECT " +
            "c.comment_id, c.user_id, c.comment_content, c.comment_date, c.like_number, c.father_id, " +
            "u.user_name, u.user_avatar, " +
            "(IF(cl.comment_like_id IS NOT NULL, 1, 0)) AS likeFlag " +
            "FROM comments c " +
            "LEFT JOIN users u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_likes cl ON c.comment_id = cl.comment_id AND cl.user_id = #{visitorId} " +
            "WHERE c.father_id = #{fatherId} AND c.comment_type = #{type}")
    @Results({
            @Result(column = "comment_id", property = "commentId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "comment_content", property = "content"),
            @Result(column = "comment_date", property = "time"),
            @Result(column = "like_number", property = "likeNum"),
            @Result(column = "father_id", property = "fatherId"),
            @Result(column = "user_name", property = "name"),
            @Result(column = "user_avatar", property = "avatar"),
            @Result(column = "likeFlag", property = "likeFlag")
    })
    List<CommentInfo> getCommentListByFatherId(@Param("visitorId") Long visitorId, @Param("fatherId") Long fatherId, @Param("type") int type);
}

