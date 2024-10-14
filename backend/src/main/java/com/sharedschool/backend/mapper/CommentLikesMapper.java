package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.CommentLikes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentLikesMapper extends BaseMapper<CommentLikes> {
    @Select("SELECT IF(COUNT(*) > 0, 1, 0) AS likeFlag FROM comment_likes WHERE comment_id = #{commentId} AND user_id = #{userId}")
    int selectLikeFlag(@Param("cardId") Long commentId, @Param("userId") Long userId);
}
