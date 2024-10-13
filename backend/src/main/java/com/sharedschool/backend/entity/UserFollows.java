package com.sharedschool.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_follows") // 表名
public class UserFollows {
    @TableId(value = "follow_id",type = IdType.AUTO) // 主键，自增
    private Long followId;
    @TableField("follower_id") // 被关注者ID
    private Long followerId;
    @TableField("following_id") // 关注者ID
    private Long followingId;
}


