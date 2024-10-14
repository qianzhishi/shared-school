package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.CommentLikes;
import com.sharedschool.backend.mapper.CommentLikesMapper;
import com.sharedschool.backend.service.CommentLikesService;
import org.springframework.stereotype.Service;

@Service
public class CommentLikesServiceImpl extends ServiceImpl<CommentLikesMapper, CommentLikes> implements CommentLikesService {
}
