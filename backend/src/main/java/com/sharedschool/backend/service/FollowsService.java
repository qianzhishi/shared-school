package com.sharedschool.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedschool.backend.typehandler.FollowsInfo;
import com.sharedschool.backend.entity.UserFollows;

import java.util.List;


public interface FollowsService extends IService<UserFollows> {
    List<FollowsInfo> getLikesList(Long userId);
    List<FollowsInfo> getFansList(Long userId);
}
