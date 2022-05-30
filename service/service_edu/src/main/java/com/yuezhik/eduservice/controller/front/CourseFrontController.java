package com.yuezhik.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.commonutils.EduCourseVo;
import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.client.OrderClient;
import com.yuezhik.eduservice.entity.EduCourse;
import com.yuezhik.eduservice.entity.chapter.ChapterVo;
import com.yuezhik.eduservice.entity.frontvo.CourseFrontVo;
import com.yuezhik.eduservice.entity.frontvo.CourseWebVo;
import com.yuezhik.eduservice.service.EduChapterService;
import com.yuezhik.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

        @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> coursePage=new Page<>(page,limit);
        Map<String,Object> map=courseService.getCourseFrontList(coursePage,courseFrontVo);

        return R.ok().data(map);
    }
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        boolean isBuyCourse = false;

        CourseWebVo courseWebVo=courseService.getBaseCourseInfo(courseId);
        //根据课程id，查询章节和小节信息
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);

        //获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        if (!StringUtils.isEmpty(memberId)){
            //根据课程id、用户id，查询课程是否已经购买
            isBuyCourse = orderClient.isBuyCourse(memberId, courseId);
        }
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",isBuyCourse);
    }
    @PostMapping("/getCourseInfoOrder/{courseId}")
    public EduCourseVo getCourseInfoOrder(@PathVariable String courseId){
        CourseWebVo baseCourseInfo = courseService.getBaseCourseInfo(courseId);
        EduCourseVo courseVo=new EduCourseVo();
        BeanUtils.copyProperties(baseCourseInfo,courseVo);

        return courseVo;
    }

}
