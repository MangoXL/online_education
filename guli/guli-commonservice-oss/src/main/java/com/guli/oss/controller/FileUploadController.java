package com.guli.oss.controller;

import com.guli.common.vo.R;
import com.guli.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/file/oss")
@Api(description = "阿里云文件管理")
@CrossOrigin //跨域
public class FileUploadController {

    @Autowired
    FileService fileService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(name = "file",value = "文件",required = true)
            @RequestParam("file") MultipartFile file){
        String uploadUrl = fileService.upload(file);
        return R.ok().data("url",uploadUrl);
    }
}
