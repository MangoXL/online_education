package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.query.TeacherQuery;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author XiaoLe
 * @since 2019-02-23
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 根据多个条件，筛选查询讲师
     * @param teacherPage
     * @param teacherQuery
     */
    void queryTeacher(Page<Teacher> teacherPage, TeacherQuery teacherQuery);


}
