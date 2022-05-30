package com.yuezhik.eduservice.mapper;

import com.yuezhik.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuezhik.eduservice.entity.frontvo.CourseWebVo;
import com.yuezhik.eduservice.entity.vo.CoursePublishVo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@Component
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo getPublishCourseInfo(String courseId);
    CourseWebVo getBaseCourseInfo(String courseId);
}
