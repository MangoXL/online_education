package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExcelImportUtil;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author XiaoLe
 * @since 2019-02-23
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<String> importBatch(MultipartFile file) {
        //创建导入失败信息集合
        List<String> list = new ArrayList<>();
        try {
            //获取文件输入流
            InputStream is = file.getInputStream();
            //使用工具类读取Excel文件
            ExcelImportUtil util = new ExcelImportUtil(is);
            HSSFSheet sheet = util.getSheet();
            //获取物理行数
            int rowCount = sheet.getPhysicalNumberOfRows();
            //判断是否有数据：不读取标题行，只读数据
            if (rowCount <= 1) {
                list.add("请填写数据！");
                return list;
            }
            //遍历每一行数据
            for (int rowNum = 1; rowNum < rowCount; rowNum++) {
                //获取行数据
                HSSFRow rowData = sheet.getRow(rowNum);
                String levelOneValue = "";
                String parentId = null;
                if (rowData != null) {
                    //获取单元格(一级分类)
                    HSSFCell levelOneCell = rowData.getCell(0);
                    if (levelOneCell != null) {
                        //获取单元数据
                        levelOneValue = util.getCellValue(levelOneCell);
                        if (StringUtils.isEmpty(levelOneValue)) {
                            //如果数据为空，返回错误提示
                            list.add("第" + rowNum + "行一级分类数据为空！");
                            continue;
                        }
                        //判断此一级分类是否已存在
                        Subject subjectLevelOne = this.getByTitle(levelOneValue);
                        if (subjectLevelOne == null) {
                            //创建次一级分类，并插入数据
                            Subject subject = new Subject();
                            subject.setTitle(levelOneValue);
                            subject.setSort(0);
                            baseMapper.insert(subject);
                            //返回此一级分类的的id作为父id返回
                            parentId = subject.getId();
                        } else {
                            //获取此一级分类的id作为父id返回
                            parentId = subjectLevelOne.getId();
                        }
                    }
                    String levelTwoValue = "";
                    //获取单元格（二级分类）
                    HSSFCell levelTwoCell = rowData.getCell(1);
                    if (levelTwoCell != null) {
                        //获取单元数据
                        levelTwoValue = util.getCellValue(levelTwoCell);
                        if (StringUtils.isEmpty(levelTwoValue)) {
                            //如果数据为空，返回错误提示
                            list.add("第" + rowNum + "行二级分类数据为空！");
                            continue;
                        }
                        //判断此二级分类是否已存在
                        Subject subjectLevelTwo = this.getByTitleAndParentId(levelTwoValue, parentId);
                        if (subjectLevelTwo == null) {
                            Subject subject = new Subject();
                            subject.setTitle(levelTwoValue);
                            subject.setParentId(parentId);
                            subject.setSort(0);
                            baseMapper.insert(subject);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.EXCEL_IMPORT_ERROR);
        }
        return list;
    }

    /**
     * 根据一级分类的名称判断该分类是否存在
     *
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("parent_Id", 0);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据二级分类的名称和父id判断次分类是否存在
     *
     * @param title
     * @param parentId
     * @return
     */
    private Subject getByTitleAndParentId(String title, String parentId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(wrapper);
    }
}
