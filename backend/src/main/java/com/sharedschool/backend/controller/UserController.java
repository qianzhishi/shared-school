package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.UserService;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class UserController {

    @Resource
    private UserService userService;

    // 登录
    @GetMapping("/frontend/system/login")
    public ApiResponse<String> login(@RequestParam Long id, @RequestParam String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        queryWrapper.eq("user_password", password);
        User user = userService.getOne(queryWrapper);
        if (user != null){
            return ApiResponse.success(user.getAvatar());
        }else {
            return ApiResponse.fail("用户名或密码错误");
        }
    }

    // 获取用户信息
    @GetMapping("/frontend/user/info")
    public ApiResponse<User> getUserInfo(@RequestParam Long id){
        // 判断是否存在该用户
        User user = userService.getById(id);
        if (user == null) {
            return ApiResponse.fail("未查询到该用户");
        }
        return ApiResponse.success(user);
    }

    // 注册
    @PostMapping("/frontend/system/register")
    public ApiResponse<Long> addUser(@RequestParam String username, @RequestParam String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.save(user);
        return ApiResponse.success(user.getUserId());
    }

    // 编辑用户信息
    @PostMapping("/frontend/user/edit")
    public ApiResponse<Void> editUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam int gender,
            @RequestParam String avatar,
            @RequestParam String intro,
            @RequestParam String contact,
            @RequestParam int age
    ){
            // 获取用户信息
            User user = userService.getById(id);
            if (user == null) {
                return ApiResponse.fail("未查询到该用户");
            }

            // 更新用户信息
            user.setUsername(username);
            user.setGender(gender);
            user.setAvatar(avatar);
            user.setIntro(intro);
            user.setContact(contact);
            user.setAge(age);

            boolean result = userService.updateById(user);

            if (result) {
                return ApiResponse.success();
            } else {
                return ApiResponse.fail("更新失败");
            }
    }

    // 修改密码
    @PostMapping("/frontend/user/editPwd")
    public ApiResponse<Void> editPwd(@RequestParam Long id, @RequestParam String oldPassword,@RequestParam String newPassword) {
        User user = userService.getById(id);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            userService.updateById(user);
            return ApiResponse.success();
        } else {
            return ApiResponse.fail("Id或密码错误");
        }
    }

    // 设置隐私
    @PostMapping("/frontend/user/setPrivacy")
    public ApiResponse<Void> setPrivacy(
            @RequestParam Long id,
            @RequestParam int showPosts,
            @RequestParam int  showComments,
            @RequestParam int showLikes,
            @RequestParam int showFans
    ){
        User user = userService.getById(id);
        if (user != null) {
            user.setShowPosts(showPosts);
            user.setShowComments(showComments);
            user.setShowLikes(showLikes);
            user.setShowFans(showFans);
            userService.updateById(user);
            return ApiResponse.success();
        } else {
            return ApiResponse.fail("未查询到该用户");
        }
    }

    // 后台获取用户信息
    @GetMapping("/backend/user/detail")
    public ApiResponse<User> getUser(@RequestParam Long id){
        User user = userService.getById(id);
        if (user != null) {
            return ApiResponse.success(user);
        } else {
            return ApiResponse.fail("未查询到该用户");
        }
    }

    // 后台获取用户列表
    @GetMapping("/backend/userList")
    public ApiResponse<PageInfo<User>> getUserList(@RequestParam int page, @RequestParam int size){
        PageHelper.startPage(page, size);
        List<User> userList = userService.list();
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        PageHelper.clearPage();
        return ApiResponse.success(pageInfo);

    }

    // 后台删除用户
    @PostMapping("/backend/delUser")
    public ApiResponse<Void> deleteUser(@RequestParam Long id){
        userService.removeById(id);
        return ApiResponse.success();
    }

}
