package com.sharedschool.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedschool.backend.entity.Resources;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.typehandler.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ResourceService extends IService<Resources> {

    ApiResponse<String> upLoadImage(MultipartFile uploadFile,Long authorId,int imageType);
    ApiResponse<Void> deleteFile(String objectName);
    ApiResponse<Void> upLoadFile(MultipartFile uploadFile,Long authorId,String filePath);
    InputStream exportOssFile(String objectName);
    List<FileInfo> getFileByPath(String path);
}
