package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.Comment;
import com.sharedschool.backend.mapper.CommentMapper;
import com.sharedschool.backend.service.CommentService;
import com.sharedschool.backend.typehandler.CommentInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public List<CommentInfo> getCommentList(Long visitorId,Long visitedId,int type) {
        return baseMapper.getCommentList(visitorId,visitedId,type);
    }

    @Override
    public List<CommentInfo> getCommentListByFatherId(Long visitorId,Long fatherId,int type) {
        return baseMapper.getCommentListByFatherId(visitorId,fatherId,type);
    }
}
