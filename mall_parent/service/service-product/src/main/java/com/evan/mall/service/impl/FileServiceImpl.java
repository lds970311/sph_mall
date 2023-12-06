package com.evan.mall.service.impl;

import com.evan.mall.common.util.DateUtil;
import com.evan.mall.service.FileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String endpointUrl, String accessKey, String secreKey, String bucketName, MultipartFile file, String fileType) throws ServerException,
            InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpointUrl)
                .credentials(accessKey, secreKey)
                .build();

        String dataString = DateUtil.getCurrentDate();
        String spuFolder = "spu/" + dataString + "/";
        String posterFolder = "poster/" + dataString + "/";
        String brandFolder = "brand/" + dataString + "/";
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
        return endpointUrl + "/" + bucketName + "/" + objectName;
    }
}
