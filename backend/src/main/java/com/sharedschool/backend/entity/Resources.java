package com.sharedschool.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("resources")
public class Resources {
    @TableId(value = "resource_id",type = IdType.AUTO)
    private Long resourceId;
    @TableField("resource_name")
    private String resourceName;
    @TableField("resource_url")
    private String resourceUrl;
    @TableField("resource_path")
    private String resourcePath;
    @TableField("resource_type")
    private int resourceType;
    @TableField("upload_date")
    private Date uploadTime;
    @TableField("author_id")
    private Long authorId;
}
