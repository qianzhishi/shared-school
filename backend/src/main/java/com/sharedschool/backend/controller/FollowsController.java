package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.typehandler.FollowsInfo;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.entity.UserFollows;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.FollowsService;
import com.sharedschool.backend.service.UserService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowsController {
    @Resource
    private FollowsService followsService;

    @Resource
    private UserService userService;

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
            @RequestParam Long focusId,
            @RequestParam Long focusedId,
            @RequestParam int state
            ){
        // state: 0:未关注 1:已关注
        if(state == 0){ // 进行关注
            UserFollows userFollows = new UserFollows();
            userFollows.setFollowingId(focusId);
            userFollows.setFollowerId(focusedId);
            followsService.save(userFollows);

            // 获取进行关注操作的用户
            User focusUser = userService.getById(focusId);
            // 关注数加1
            focusUser.setFocusNum(focusUser.getFocusNum() + 1);
            userService.updateById(focusUser);

            // 获取被关注的用户
            User focusedUser = userService.getById(focusedId);
            // 被关注用户的粉丝数加1
            focusedUser.setFansNum(focusedUser.getFansNum() + 1);
            userService.updateById(focusedUser);
        }else { // 取消关注
            // 构造查询条件
            QueryWrapper<UserFollows> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follower_id", focusedId)
                    .eq("following_id", focusId);
            // 删除关注记录
            followsService.remove(queryWrapper);

            // 获取进行取消关注操作的用户
            User focusUser = userService.getById(focusId);
            // 关注数减1
            focusUser.setFocusNum(focusUser.getFocusNum() - 1);
            userService.updateById(focusUser);

            // 获取被取消关注的用户
            User focusedUser = userService.getById(focusedId);
            // 粉丝数减1
            focusedUser.setFansNum(focusedUser.getFansNum() - 1);
            userService.updateById(focusedUser);
        }
        return ApiResponse.success();
    }
}
