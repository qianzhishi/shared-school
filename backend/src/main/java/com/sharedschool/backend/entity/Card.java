package com.sharedschool.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jfr.ContentType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("card")
public class Card {
    @TableId(value = "card_id",type = IdType.AUTO)
    private Long cardId;
    @TableField("user_id")
    private Long userId;
    @TableField("card_type")
    private int type;
    @TableField("card_title")
    private String title;
    @TableField(value = "card_content",typeHandler = JacksonTypeHandler.class)
    public String content;
    @TableField("card_date")
    private Date date;
    @TableField("card_view_number")
    private Long viewNum = 0L;
    @TableField("card_like_number")
    private Long likeNum = 0L;
    @TableField("card_comment_number")
    private Long commentNum = 0L;
    @TableField(value = "card_images",typeHandler = JacksonTypeHandler.class)
    private List<String> images;

}

