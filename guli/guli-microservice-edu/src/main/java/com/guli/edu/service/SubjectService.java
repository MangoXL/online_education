package com.guli.edu.service;

import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author XiaoLe
 * @since 2019-02-23
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 上传文件，如果有文件导入失败，返回失败记录
     * @param file
     * @return
     */
    List<String> importBatch(MultipartFile file);
}
