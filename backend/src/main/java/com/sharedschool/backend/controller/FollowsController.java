package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.FollowsInfo;
import com.sharedschool.backend.entity.UserFollows;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.FollowsService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
public class FollowsController {
    @Resource
    private FollowsService followsService;

    @GetMapping("/frontend/user/likes")
    public ApiResponse<List<FollowsInfo>> getLikesList(@RequestParam Long userId){
        List<FollowsInfo> likesList = followsService.getLikesList(userId);
        return ApiResponse.success(likesList);
    }

    @GetMapping("/frontend/user/fans")
    public ApiResponse<List<FollowsInfo>> getFansList(@RequestParam Long userId) {
        List<FollowsInfo> followsList = followsService.getFansList(userId);
        return ApiResponse.success(followsList);
    }

    @PostMapping("/frontend/user/follow")
    public ApiResponse<Void> followUser(
            @RequestParam Long followingId,
            @RequestParam Long followerId,
            @RequestParam int state
            ){
        // state: 0:未关注 1:已关注
        if(state == 0){ // 进行关注
            UserFollows userFollows = new UserFollows();
            userFollows.setFollowingId(followingId);
            userFollows.setFollowerId(followerId);
            followsService.save(userFollows);
        }else { // 取消关注
            QueryWrapper<UserFollows> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follower_id", followerId)
                    .eq("following_id", followingId);
            followsService.remove(queryWrapper);
        }
        return ApiResponse.success();
    }
}
