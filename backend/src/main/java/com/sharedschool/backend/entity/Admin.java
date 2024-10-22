package com.sharedschool.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("administrators")
public class Admin {
    @TableId(value = "admin_id",type = IdType.AUTO)
    private Long adminId;
    @TableField("admin_name")
    private String adminName;
    @TableField("admin_password")
    private String adminPassword;
}
