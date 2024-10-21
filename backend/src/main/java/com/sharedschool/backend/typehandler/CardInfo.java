package com.sharedschool.backend.typehandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CardInfo {
    private Long cardId;
    private Long userId;
    private int type;
    private String name;
    private String avatar;
    private String title;
    public String content;
    private List<String> images = null;
    private Date time;
    private Long viewNum;
    private Long likeNum;
    private Long commentNum;
    private int likeFlag = 0;
}
