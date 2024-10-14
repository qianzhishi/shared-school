package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sharedschool.backend.typehandler.FollowsInfo;
import com.sharedschool.backend.entity.UserFollows;
import com.sharedschool.backend.mapper.FollowsMapper;
import com.sharedschool.backend.service.FollowsService;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, UserFollows> implements FollowsService {
    @Override
    public List<FollowsInfo> getLikesList(Long userId) {
        return baseMapper.getLikesList(userId);
    }
    @Override
    public List<FollowsInfo> getFansList(Long userId) {
        return baseMapper.getFansList(userId);
    }
}
