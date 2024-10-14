package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.FollowsInfo;
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

@Data
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

            User focusUser = userService.getById(focusId);
            focusUser.setFocusNum(focusUser.getFocusNum() + 1);
            userService.updateById(focusUser);

            User focusedUser = userService.getById(focusedId);
            focusedUser.setFansNum(focusedUser.getFansNum() + 1);
            userService.updateById(focusedUser);
        }else { // 取消关注
            QueryWrapper<UserFollows> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follower_id", focusedId)
                    .eq("following_id", focusId);
            followsService.remove(queryWrapper);

            User focusUser = userService.getById(focusId);
            focusUser.setFocusNum(focusUser.getFocusNum() - 1);
            userService.updateById(focusUser);

            User focusedUser = userService.getById(focusedId);
            focusedUser.setFansNum(focusedUser.getFansNum() - 1);
            userService.updateById(focusedUser);
        }
        return ApiResponse.success();
    }
}
