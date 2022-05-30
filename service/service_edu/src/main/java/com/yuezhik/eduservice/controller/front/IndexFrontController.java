package com.yuezhik.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.EduCourse;
import com.yuezhik.eduservice.entity.EduTeacher;
import com.yuezhik.eduservice.service.EduCourseService;
import com.yuezhik.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    @GetMapping("/index")
    public R index(){
        List<EduCourse> eduList = courseService.getIndex();

        List<EduTeacher> teacherList = teacherService.getIndex();

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
