package com.sharedschool.backend.typehandler;

import com.sharedschool.backend.entity.Card;
import lombok.Data;

import java.util.List;

@Data
public class CardDetailInfo {

    private Card cardInfo;
    private String name;
    private String avatar;
    private int likeFlag = 0; // 0:未点赞 1:已点赞
    List<CommentInfo> commentList;
}
