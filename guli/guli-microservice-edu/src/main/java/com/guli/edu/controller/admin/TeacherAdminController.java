package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/teacher")
@Api(description = "讲师管理")
public class TeacherAdminController {
    @Autowired
    TeacherService teacherService;

    @ApiOperation(value = "查询所有讲师")
    @GetMapping
    public R list(){
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("list",list);
    }

    @ApiOperation(value = "删除指定讲师")
    @DeleteMapping("{id}")
    public R deleteById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        teacherService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page",value = "当前页面",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit){
        //判断页码数据是否异常
        if(page <= 0 || limit <= 0){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        Page<Teacher> teacherPage = new Page<>(page,limit);
        teacherService.page(teacherPage, null);
        List<Teacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }

    @ApiOperation(value = "根据条件筛选并分页查询讲师")
    @GetMapping("query/{page}/{limit}")
    public R queryList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(name = "teacherQuery",value = "综合查询条件对象",required = false)
            TeacherQuery teacherQuery){
        //判断页码数据是否异常
        if(page <= 0 || limit <= 0){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        Page<Teacher> teacherPage = new Page<>(page,limit);
        teacherService.queryTeacher(teacherPage,teacherQuery);
        List<Teacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping
    public R addTeacher(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher){
        teacherService.save(teacher);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    @ApiOperation(value = "根据id修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher){
        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }

}
