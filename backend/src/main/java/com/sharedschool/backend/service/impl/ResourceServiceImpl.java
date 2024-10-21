package com.sharedschool.backend.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.sharedschool.backend.config.AliyunConfig;
import com.sharedschool.backend.entity.Resources;
import com.sharedschool.backend.mapper.ResourceMapper;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.ResourceService;
import com.sharedschool.backend.typehandler.FileInfo;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper,Resources> implements ResourceService {
    // 允许上传的图片格式
    private static final String[] IMAGE_TYPE = new String[]{".jpg", ".jpeg", ".png"};
    @Resource
    private OSS ossClient;
    @Resource
    private AliyunConfig aliyunConfig;

    // objectName: 要存储的对象的名称，这里的对象可以是字符串、图片、视频等
    // 方法内部可能发生变化的是objectName和输入流，因此以参数的方式暴露出去
    // 方法要返回图片在服务器上的访问地址，因此返回值是String类型
    @Override
    public ApiResponse<String> upLoadImage(MultipartFile uploadFile,Long authorId,int imageType) {
        // 校验图片格式
        boolean isLegal = false;
        String fileName = uploadFile.getOriginalFilename();
        if (fileName == null) {
            return ApiResponse.fail("文件名不能为空");
        }
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(fileName, type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            return ApiResponse.fail("图片格式不正确");
        }
        //文件新路径
        String filePath = "avatarImages/" + changeFileName(fileName);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            return ApiResponse.fail("上传失败");
        }
        Resources resources = new Resources();
        resources.setResourceUrl(aliyunConfig.getUrlPrefix()+filePath);
        resources.setResourcePath(filePath);
        resources.setResourceType(imageType);
        resources.setResourceName(fileName);
        resources.setUploadTime(new Date());
        resources.setAuthorId(authorId);
        this.save(resources);

        return ApiResponse.success(aliyunConfig.getUrlPrefix()+filePath);
    }

    @Override
    public List<FileInfo> getFileByPath(String path) {
        return baseMapper.getFileByPath(path);
    }

    // 上传文件
    @Override
    public ApiResponse<Void> upLoadFile(MultipartFile uploadFile,Long authorId,String filePathPrefix) {
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        if (fileName == null) {
            return ApiResponse.fail("文件名不能为空");
        }
        String filePath = filePathPrefix + changeFileName(fileName);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            return ApiResponse.fail("上传失败");
        }
        Resources resources = new Resources();
        resources.setResourceUrl(aliyunConfig.getUrlPrefix()+filePath);
        resources.setResourcePath(filePath);
        resources.setResourceType(3);
        resources.setResourceName(fileName);
        resources.setUploadTime(new Date());
        resources.setAuthorId(authorId);
        this.save(resources);

        return ApiResponse.success();

    }

    // 导出文件
    @Override
    public InputStream exportOssFile(String objectName) {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getBucketName(), objectName);
        // 读取文件内容。
        return ossObject.getObjectContent();
    }

    // 删除文件
    @Override
    public ApiResponse<Void> deleteFile(String objectName) {

        ossClient.deleteObject(aliyunConfig.getBucketName(), objectName);
        return ApiResponse.success();
    }

    //使用UUID生成文件名，保证文件名唯一，从而防止文件被覆盖
    private String changeFileName(String originalFilename) {
        //originalFilename.lastIndexOf("."): 从文件名的最后一个"."处开始截取字符串(包括".")，即获取原始文件后缀名
        return UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
    }

}
