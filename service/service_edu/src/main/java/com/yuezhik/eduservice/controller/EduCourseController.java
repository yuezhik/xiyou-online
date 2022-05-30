package com.yuezhik.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.EduCourse;
import com.yuezhik.eduservice.entity.vo.CourseInfoVo;
import com.yuezhik.eduservice.entity.vo.CoursePublishVo;
import com.yuezhik.eduservice.entity.vo.CourseQuery;
import com.yuezhik.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/eduservice/course")
@Api(description = "课程信息管理")
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    @PostMapping("/addCourseInfo")
    @ApiOperation(value = "根据表单添加课程信息")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = eduCourseService.saveCourseInfo(courseInfoVo);

        return R.ok().data("courseId",id);
    }
    @GetMapping("/getCourseInfo/{courseId}")
    @ApiOperation(value = "根据id获取课程信息")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo=eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfoVo);
    }
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }
    //根据课程id查询课程确认信息
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo publishCourseInfo = eduCourseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourse",publishCourseInfo);
    }
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse=new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        boolean flag = eduCourseService.updateById(eduCourse);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }
    @GetMapping("/getCourseList")
    public R getCourseList(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }
    //条件查询
    @PostMapping("/pageCourseCondition/{page}/{limit}")
    public R pageCourseCondition(@PathVariable Long page, @PathVariable long limit,
                                 @RequestBody(required = false)CourseQuery courseQuery){
        Page<EduCourse> pageParam=new Page<>(page,limit);
        eduCourseService.pageQuery(pageParam,courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }
    @DeleteMapping("/{id}")
    public R removeCourseById(@PathVariable String id){
        boolean flag = eduCourseService.removeCourse(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }


}

