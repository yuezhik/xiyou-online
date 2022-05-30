package com.yuezhik.eduservice.controller;


import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.client.VodClient;
import com.yuezhik.eduservice.entity.EduVideo;
import com.yuezhik.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/eduservice/video")
public class    EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }


    //删除小节
    @DeleteMapping("/{id}")
    public R deleteVideo(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeAlyVideo(videoSourceId);
        }
        eduVideoService.removeById(id);
        return R.ok();
    }

    //修改小节
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    //根据小节id查询
    @PostMapping("/getVideoInfo/{videoId}")
    public R getVideoById(@PathVariable String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("video",eduVideo);
    }
}

