package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin/product")
@Slf4j
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

    @PostMapping("/fileUpload/{fileType}")
    public Result<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                           @PathVariable String fileType) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(this.endpointUrl)
                .credentials(this.accessKey, this.secreKey)
                .build();


        String spuFolder = "spu/";
        String posterFolder = "poster/";
        String brandFolder = "brand/";
        String objectName;
        if ("spu".equals(fileType)) {
            objectName = spuFolder + System.currentTimeMillis() + UUID.randomUUID();
        } else if ("brand".equals(fileType)) {
            objectName = brandFolder + System.currentTimeMillis() + UUID.randomUUID();
        } else {
            objectName = posterFolder + System.currentTimeMillis() + UUID.randomUUID();
        }

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        String endpointUrl = this.endpointUrl + "/" + this.bucketName + "/" + objectName;
        return Result.ok(endpointUrl);
    }
}
