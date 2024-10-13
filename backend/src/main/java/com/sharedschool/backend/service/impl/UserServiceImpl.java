package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.User;
import com.sharedschool.backend.mapper.UserMapper;
import com.sharedschool.backend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
