package com.yuezhik.eduservice.client;

import com.yuezhik.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name="service-vod",fallback = VodClientImpl.class)
public interface VodClient {
    @DeleteMapping("eduvod/video/removeAlyVideo/{videoId}")
    R removeAlyVideo(@PathVariable("videoId") String videoId);

    //根据多个视频id删除多个阿里云视频
    @DeleteMapping("/eduvod/video/removeBatch")
    public R removeBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
