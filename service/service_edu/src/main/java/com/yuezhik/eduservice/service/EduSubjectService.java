package com.yuezhik.eduservice.service;

import com.yuezhik.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuezhik.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-11
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, EduSubjectService eduSubjectService);
    List<OneSubject> getAllOneTwoSubject();
}
