package com.yuezhik.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-03-26
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> getIndex();

    Map<String, Object> getTeacherFrontPageList(Page<EduTeacher> teacherPage);
}
