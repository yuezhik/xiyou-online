package com.yuezhik.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.eduservice.entity.EduCourse;
import com.yuezhik.eduservice.entity.EduCourseDescription;
import com.yuezhik.eduservice.entity.frontvo.CourseFrontVo;
import com.yuezhik.eduservice.entity.frontvo.CourseWebVo;
import com.yuezhik.eduservice.entity.vo.CourseInfoVo;
import com.yuezhik.eduservice.entity.vo.CoursePublishVo;
import com.yuezhik.eduservice.entity.vo.CourseQuery;
import com.yuezhik.eduservice.mapper.EduCourseMapper;
import com.yuezhik.eduservice.service.EduChapterService;
import com.yuezhik.eduservice.service.EduCourseDescriptionService;
import com.yuezhik.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuezhik.eduservice.service.EduVideoService;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert==0){
            throw new GuliException(20001,"添加失败");
        }
        String cid=eduCourse.getId();
        EduCourseDescription courseDescription=new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;

    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update==0){
            throw new GuliException(20001,"修改课程信息失败");
        }

        EduCourseDescription description=new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.saveOrUpdate(description);
    }
    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo getPublishCourseInfo(String courseId) {
        return baseMapper.getPublishCourseInfo(courseId);
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {

        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();

        String title=courseQuery.getTitle();
        String status=courseQuery.getStatus();
        if(!StringUtils.isEmpty(courseQuery.getTitle())){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(courseQuery.getStatus())){
            wrapper.eq("status",status);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //带上门判断后的条件进行分页查询
        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public boolean removeCourse(String id) {
        //删除小节
        videoService.removeByCourseId(id);
        //删除章节
        chapterService.removeByCourseId(id);
        //删除简介
        courseDescriptionService.removeById(id);
        //删除课程本身
        int result = baseMapper.deleteById(id);

        if (result>=1){
            return true;
        }else {
            throw new GuliException(20001,"删除失败");
        }
    }

    //获取首页热门课程数据，方便redis进行缓存
    @Override
    @Cacheable(key="'courseList'",value = "courses")
    public List<EduCourse> getIndex() {
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        String title = null;
        String subjectId = null;
        String subjectParentId = null;
        String gmtCreateSort = null;
        String buyCountSort = null;
        String priceSort = null;
        String teacherId = null;

        if (!StringUtils.isEmpty(courseFrontVo)){
            title = courseFrontVo.getTitle();
            subjectId = courseFrontVo.getSubjectId();
            subjectParentId = courseFrontVo.getSubjectParentId();
            gmtCreateSort = courseFrontVo.getGmtCreateSort();
            buyCountSort = courseFrontVo.getBuyCountSort();
            priceSort = courseFrontVo.getPriceSort();
            teacherId = courseFrontVo.getTeacherId();
        }


        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接条件
        if (!StringUtils.isEmpty(subjectParentId)){//一级分类
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)){//二级分类
            wrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)){//关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(priceSort)){//价格
            wrapper.orderByDesc("price");
        }
        if (!StringUtils.isEmpty(gmtCreateSort)){//最新，创建时间
            wrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(pageCourse, wrapper);

        long total = pageCourse.getTotal();//总记录数
        List<EduCourse> courseList = pageCourse.getRecords();//数据集合
        long size = pageCourse.getSize();//每页记录数
        long current = pageCourse.getCurrent();//当前页
        long pages = pageCourse.getPages();//总页数
        boolean hasPrevious = pageCourse.hasPrevious();//是否有上一页
        boolean hasNext = pageCourse.hasNext();//是否有下一页

        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("list",courseList);
        map.put("size",size);
        map.put("current",current);
        map.put("pages",pages);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);

        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
