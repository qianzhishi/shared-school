package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT user_name FROM users WHERE user_id = #{userId}")
    String selectName(@Param("userId") Long userId);

    @Select("SELECT user_avatar FROM users WHERE user_id = #{userId}")
    String selectAvatar(@Param("userId") Long userId);
}
