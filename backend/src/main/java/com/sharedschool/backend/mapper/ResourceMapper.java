package com.sharedschool.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedschool.backend.entity.Resources;
import com.sharedschool.backend.typehandler.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ResourceMapper extends BaseMapper<Resources> {

    @Select({
            "SELECT "+
            "r.resource_path,r.resource_name,r.author_id,r.upload_date, "+
            "u.user_name "+
            "FROM resources r "+
            "LEFT JOIN users u ON r.author_id = u.user_id "+
            "WHERE r.resource_path LIKE CONCAT(#{path},'%') "+
            "AND r.resource_path NOT LIKE CONCAT(#{path},'%/%') "
    })
    @Results({
            @Result(column = "resource_name", property = "name"),
            @Result(column = "upload_date", property = "date"),
            @Result(column = "user_name", property = "author"),
            @Result(column = "resource_path", property = "pathUrl"),
    })
    List<FileInfo> getFileByPath(@Param("path") String path);

}
