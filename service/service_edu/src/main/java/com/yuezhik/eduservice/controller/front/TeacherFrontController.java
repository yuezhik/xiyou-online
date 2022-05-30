package com.yuezhik.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.EduCourse;
import com.yuezhik.eduservice.entity.EduTeacher;
import com.yuezhik.eduservice.service.EduCourseService;
import com.yuezhik.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontPageList(@PathVariable long page,@PathVariable long limit){
        Page<EduTeacher> teacherPage=new Page<>(page,limit);
        Map<String, Object> map = teacherService.getTeacherFrontPageList(teacherPage);
        return R.ok().data(map);
    }
    @GetMapping("/getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        EduTeacher teacher = teacherService.getById(teacherId);

        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }
}
