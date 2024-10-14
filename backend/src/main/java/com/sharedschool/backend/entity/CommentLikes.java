package com.sharedschool.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("comment_likes")
public class CommentLikes {
    @TableId(value = "comment_like_id",type = IdType.AUTO)
    private Long likeId;
    @TableField("comment_id")
    private Long commentId;
    @TableField("user_id")
    private Long userId;
}
