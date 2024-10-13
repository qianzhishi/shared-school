package com.sharedschool.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;
    @TableField("user_name")
    private String username;
    @TableField("user_password")
    private String password;
    @TableField("user_gender")
    private int gender = 0;
    @TableField("user_avatar")
    private String avatar = "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
    @TableField("user_intro")
    private String intro = "这个人很懒，什么也没留下";
    @TableField("user_contact")
    private String contact = "暂无联系方式";
    @TableField("user_age")
    private int age = 0;
    @TableField("user_like_number")
    private Long likeNum = 0L;
    @TableField("user_focus_number")
    private Long focusNum = 0L;
    @TableField("user_fans_number")
    private Long fansNum = 0L;
    @TableField("show_posts")
    private int showPosts = 1;
    @TableField("show_comments")
    private int showComments = 1;
    @TableField("show_likes")
    private int showLikes = 1;
    @TableField("show_fans")
    private int showFans = 1;

}
