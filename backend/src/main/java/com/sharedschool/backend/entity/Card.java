package com.sharedschool.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Content content;
    @TableField("card_date")
    private Date date;
    @TableField("card_view_number")
    private int viewNum = 0;
    @TableField("card_like_number")
    private int likeNum = 0;
    @TableField("card_comment_number")
    private int commentNum = 0;
    @TableField(value = "card_images",typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    //内容对象
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Content {
        private String content;
    }
}

