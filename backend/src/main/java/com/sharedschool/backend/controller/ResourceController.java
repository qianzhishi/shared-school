package com.sharedschool.backend.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharedschool.backend.entity.Resources;
import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.service.ResourceService;
import com.sharedschool.backend.typehandler.FileInfo;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class ResourceController {
    @Resource
    private ResourceService resourceService;


    // 获取用户上传的资源列表(不包含上传的图片)
    @GetMapping("/frontend/upload/resourceList")
    public ApiResponse<List<Resources>> getResourceList() {
        QueryWrapper<Resources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", 3);
        return ApiResponse.success(resourceService.list(queryWrapper));
    }

    // 根据resource_path获取文件信息
    @GetMapping("/frontend/upload/getFileByPath")
    public ApiResponse<List<FileInfo>> getFileByPath(@RequestParam("path") String path) {
        return ApiResponse.success(resourceService.getFileByPath(path));
    }

    // 上传图片
    @PostMapping("/frontend/upload/image")
    public ApiResponse<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("authorId") Long authorId,
            @RequestParam("type") int type
    ){
        return resourceService.upLoadImage(file,authorId,type);
    }

    // 上传资源
     @PostMapping("/frontend/upload/resource")
     public ApiResponse<Void> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("authorId") Long authorId,
            @RequestParam("filePathPreFix") String filePathPreFix
     ){
            return resourceService.upLoadFile(file,authorId,filePathPreFix);
     }

     // 下载资源
     @GetMapping("/frontend/upload/downloadFile")
     public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("objectName") String objectName) {
            try  {
                InputStream is = this.resourceService.exportOssFile(objectName);
                if (is == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                    // 设置响应头 以流的形式下载文件,这样可以实现任意格式的文件下载
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    // 处理中文文件名乱码问题
                    String encodedFileName = new String(objectName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                    headers.setContentDispositionFormData("attachment", encodedFileName);

                    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(is));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

     }


    // 删除用户上传的图片
    @PostMapping("/frontend/upload/removeImage")
    public ApiResponse<Void> removeAvatar(@RequestParam("userId") Long userId) {
        QueryWrapper<Resources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId).eq("resource_type", 1);
        List<Resources> resourcesList = resourceService.list(queryWrapper);
        if(resourcesList.size() > 1) {
            // 删除除了最新头像之外的所有头像
            for (int i = 0; i < resourcesList.size()-1; i++) {
                Resources resource = resourcesList.get(i);
                ApiResponse<Void> result = resourceService.deleteFile(resource.getResourcePath());
                if (result.getCode() != 200) {
                    return ApiResponse.fail("删除服务器上的文件时出错！");
                }
            }
            // 删除数据库中的头像记录
            resourceService.remove(queryWrapper);
        }
        return ApiResponse.success();
    }

    // 删除文件
    @PostMapping("/frontend/upload/delResource")
    public ApiResponse<Void> delResource(@RequestParam("filePath") String filePath) {
        QueryWrapper<Resources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_path", filePath);
        resourceService.remove(queryWrapper);

        return resourceService.deleteFile(filePath);
    }


}
