package com.yuezhik.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuezhik.eduservice.entity.frontvo.CourseFrontVo;
import com.yuezhik.eduservice.entity.frontvo.CourseWebVo;
import com.yuezhik.eduservice.entity.vo.CourseInfoVo;
import com.yuezhik.eduservice.entity.vo.CoursePublishVo;
import com.yuezhik.eduservice.entity.vo.CourseQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);
    public CoursePublishVo getPublishCourseInfo(String courseId);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean removeCourse(String id);

    List<EduCourse> getIndex();

    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
