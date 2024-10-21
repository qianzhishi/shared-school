package com.sharedschool.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.typehandler.CommentInfo;

import java.util.List;

public interface CommentService extends IService<Comment> {
    List<CommentInfo> getCommentList(Long visitorId,Long visitedId,int type);
    List<CommentInfo> getCommentListByFatherId(Long visitorId,Long fatherId,int type);
}
