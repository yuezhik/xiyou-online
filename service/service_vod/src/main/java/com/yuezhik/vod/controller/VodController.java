package com.yuezhik.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.yuezhik.commonutils.R;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import com.yuezhik.vod.service.VodService;
import com.yuezhik.vod.utils.ConstantVodUtils;
import com.yuezhik.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
public class VodController {
    @Autowired
    VodService vodService;

    @PostMapping("/uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file){
        String videoId=vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }
    @DeleteMapping("/removeAlyVideo/{videoId}")
    public R removeAlyVideo(@PathVariable String videoId){
        try {
            DefaultAcsClient client= InitVodClient.initVodClient(ConstantVodUtils.KEY_ID,ConstantVodUtils.KEY_SECRET);
            DeleteVideoRequest request=new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }
    //根据id删除多个阿里云视频
    @DeleteMapping("/removeBatch")
    public R removeBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreVideo(videoIdList);
        return R.ok();
    }
    //根据视频id获取视频凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            String playAuth = vodService.getPlayAuth(id);
            return R.ok().data("PlayAuth",playAuth);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"获取视频凭证失败");
        }

    }



}
