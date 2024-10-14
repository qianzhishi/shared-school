package com.sharedschool.backend.typehandler;

import lombok.Data;

import java.util.Date;

@Data
public class CommentInfo {
    private Long commentId;
    private Long userId;
    private Long fatherId;
    private String name;
    private String avatar;
    private String content;
    private Long likeNum;
    private Date time;
    private int likeFlag;
}
