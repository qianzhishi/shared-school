package com.sharedschool.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("card_likes")
public class CardLikes {
    @TableId(value = "card_like_id",type = IdType.AUTO)
    private Long likeId;
    @TableField("card_id")
    private Long cardId;
    @TableField("user_id")
    private Long userId;
}
