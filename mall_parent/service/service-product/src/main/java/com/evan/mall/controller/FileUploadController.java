package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.service.FileService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/product")
@Slf4j
@Api(tags = "MinIO文件上传")
public class FileUploadController {

    //  获取文件上传对应的地址
    @Value("${minio.endpointUrl}")
    public String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    @Autowired
    private FileService fileService;

    @PostMapping("/fileUpload/{fileType}")
    public Result<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                           @PathVariable String fileType) throws Exception {
        String endpointUrl = this.fileService.uploadFile(this.endpointUrl, this.accessKey, this.secreKey, this.bucketName, file, fileType);
        return Result.ok(endpointUrl);
    }

}
