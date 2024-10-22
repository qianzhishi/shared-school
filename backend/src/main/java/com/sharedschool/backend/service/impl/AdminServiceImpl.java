package com.sharedschool.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedschool.backend.entity.Admin;
import com.sharedschool.backend.mapper.AdminMapper;
import com.sharedschool.backend.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {

}
