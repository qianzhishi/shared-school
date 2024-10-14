package com.sharedschool.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.typehandler.FollowsInfo;
import com.sharedschool.backend.entity.UserFollows;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowsMapper extends BaseMapper<UserFollows> {
    @Select("SELECT uf.follower_id, u.user_name, u.user_avatar, u.user_intro " +
            "FROM user_follows uf " +
            "JOIN users u ON uf.follower_id = u.user_id " +
            "WHERE uf.following_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "follower_id"),
            @Result(property = "username", column = "user_name"),
            @Result(property = "avatar", column = "user_avatar"),
            @Result(property = "intro", column = "user_intro")
    })
    List<FollowsInfo> getLikesList(Long userId);

    @Select("SELECT uf.following_id, u.user_name, u.user_avatar, u.user_intro " +
            "FROM user_follows uf " +
            "JOIN users u ON uf.following_id = u.user_id " +
            "WHERE uf.follower_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "following_id"),
            @Result(property = "username", column = "user_name"),
            @Result(property = "avatar", column = "user_avatar"),
            @Result(property = "intro", column = "user_intro")
    })
    List<FollowsInfo> getFansList(Long userId);

}
