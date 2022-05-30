package com.yuezhik.eduservice.controller;


import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.EduChapter;
import com.yuezhik.eduservice.entity.chapter.ChapterVo;
import com.yuezhik.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
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
@Api(description = "课程简介管理")
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;

    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo",list);
    }
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter chapter = eduChapterService.getById(chapterId);
        return R.ok().data("chapter",chapter);
    }
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }
    @DeleteMapping("/{chapterId}")
    public R deleteById(@PathVariable String chapterId){
        boolean flag = eduChapterService.deleteChapter(chapterId);
        if(flag) {
            return R.ok();
        }else{
            return R.error();
        }
    }


}

