package com.sharedschool.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comments")
public class Comment {
    @TableId(value = "comment_id",type = IdType.AUTO)
    private Long commentId;
    @TableField("user_id")
    private Long userId;
    @TableField("father_id")
    private Long fatherId = 0L;
    @TableField("comment_type")
    private int type = 0;
    @TableField("comment_content")
    private String content;
    @TableField("comment_date")
    private Date date;
    @TableField("like_number")
    private Long likeNum = 0L;
}
