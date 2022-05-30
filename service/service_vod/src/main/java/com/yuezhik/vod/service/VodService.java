package com.yuezhik.vod.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface VodService {
    String uploadVideoAly(MultipartFile file);
    //根据id删除多个阿里云视频
    void removeMoreVideo(List<String> videoIdList);

    String getPlayAuth(String id);
}
