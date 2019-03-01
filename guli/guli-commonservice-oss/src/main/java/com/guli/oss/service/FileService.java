package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传文件到阿里云OSS
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
