package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/edu/subject")
@Api(description = "课程分类管理")
@CrossOrigin //跨域
public class SubjectAdminController {

    @Autowired
    SubjectService subjectService;

    @ApiOperation("Excel批量导入")
    @PostMapping("import")
    public R importExcel(
            @ApiParam(name = "file",value = "Excel文件",required = true)
            @RequestParam("file") MultipartFile file){
        List<String> messageList = subjectService.importBatch(file);
        if(messageList.size() == 0){
            //上传成功
            return R.ok().message("文件上传成功！");
        }else{
            //上传失败并返回失败记录
            return R.error().message("部分数据上传失败！").data("messageList",messageList);
        }
    }
}
