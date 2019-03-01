package com.guli.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String uploadUrl = null;
        try {
            //获取阿里云配置信息
            String endPoint = ConstantPropertiesUtil.END_POINT;
            String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            String fileHost = ConstantPropertiesUtil.FILE_HOST;
            //创建OSSClient实例
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            //判断该实例对象是否存在
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            //获取文件上传流
            InputStream inputStream = file.getInputStream();
            //上传图片格式为：avatar/2019/02/28/uuid.扩展名
            //日期路径
           String datePath = new DateTime().toString("yyyy/MM/dd");
            //原文件名
            String originalFilename = file.getOriginalFilename();
            //原文件扩展名
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            //新建文件名
            String fileName = UUID.randomUUID().toString();
            String newFile = fileName + fileType;
            //新文件名拼接原文件扩展名
            String fileUrl = fileHost + "/" + datePath + "/" + newFile;
            //上传文件
            ossClient.putObject(bucketName, fileUrl, inputStream);
            //关闭
            inputStream.close();
            ossClient.shutdown();
            //返回文件的完整URL地址
            uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
        return uploadUrl;
    }
}
