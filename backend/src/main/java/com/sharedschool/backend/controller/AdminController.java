package com.sharedschool.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sharedschool.backend.entity.Admin;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Resource
    private AdminService adminService;

    // 后台登录接口
    @GetMapping("/backend/login")
    public ApiResponse<String> login(@RequestParam String adminId, @RequestParam String password) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_id", adminId).eq("admin_password", password);
        Admin admin = adminService.getOne(queryWrapper);
        if (admin != null) {
            return ApiResponse.success(admin.getAdminName());
        } else {
            return ApiResponse.fail("用户名或密码错误");
        }
    }

    // 后台更新管理员信息接口
    @PostMapping("/backend/updateAdmin")
    public ApiResponse<Void> updateAdmin(
            @RequestParam Long adminId,
            @RequestParam String name,
            @RequestParam String password
    ) {
        Admin admin = adminService.getById(adminId);
        if (admin != null) {
            admin.setAdminName(name);
            admin.setAdminPassword(password);
            adminService.updateById(admin);
            return ApiResponse.success();
        } else {
            return ApiResponse.fail("管理员不存在");
        }
    }

    // 后台添加管理员接口
    @PostMapping("/backend/addAdmin")
    public ApiResponse<Void> addAdmin(
            @RequestParam String name,
            @RequestParam String password
    ) {
        Admin admin = new Admin();
        admin.setAdminName(name);
        admin.setAdminPassword(password);
        adminService.save(admin);
        return ApiResponse.success();
    }

    // 后台获取管理员列表接口
    @GetMapping("/backend/adminList")
    public ApiResponse<PageInfo<Admin>> getAdminList(
            @RequestParam int page,
            @RequestParam int size
    ) {
        PageHelper.startPage(page, size);
        List<Admin> adminList = adminService.list();
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);
        return ApiResponse.success(pageInfo);
    }

    // 后台删除管理员接口
    @PostMapping("/backend/delAdmin")
    public ApiResponse<Void> deleteAdmin(@RequestParam Long adminId) {
        adminService.removeById(adminId);
        return ApiResponse.success();
    }
}
