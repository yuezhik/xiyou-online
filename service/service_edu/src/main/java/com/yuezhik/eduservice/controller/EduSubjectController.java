package com.yuezhik.eduservice.controller;


import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.subject.OneSubject;
import com.yuezhik.eduservice.service.EduSubjectService;
import com.yuezhik.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-11
 */
@RestController
@RequestMapping("/eduservice/subject")
@Api(description = "课程分类管理")
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("allSubject",list);
    }
}

