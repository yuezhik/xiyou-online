package com.yuezhik.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.eduservice.entity.EduTeacher;
import com.yuezhik.eduservice.mapper.EduTeacherMapper;
import com.yuezhik.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-03-26
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    @Cacheable(value = "teachers",key = "'teacherList'")
    public List<EduTeacher> getIndex() {
        QueryWrapper<EduTeacher> wrapperTeacher=new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(wrapperTeacher);
        return eduTeachers;

    }

    @Override
    public Map<String, Object> getTeacherFrontPageList(Page<EduTeacher> teacherPage) {
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        baseMapper.selectPage(teacherPage, wrapper);

        HashMap<String, Object> map = new HashMap<>();

        //总记录数
        long total = teacherPage.getTotal();
        //当前页
        long current = teacherPage.getCurrent();
        //每页记录数
        long size = teacherPage.getSize();
        //查询到的对象
        List<EduTeacher> teacherList = teacherPage.getRecords();
        //总页数
        long pages = teacherPage.getPages();
        //是否有上一页
        boolean hasPrevious = teacherPage.hasPrevious();
        //是否有下一页
        boolean hasNext = teacherPage.hasNext();

        map.put("total",total);
        map.put("current",current);
        map.put("size",size);
        map.put("items",teacherList);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);
        map.put("pages",pages);

        return map;


    }
}
