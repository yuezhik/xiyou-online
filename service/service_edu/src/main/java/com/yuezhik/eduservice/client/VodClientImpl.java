package com.yuezhik.eduservice.client;

import com.yuezhik.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodClientImpl implements VodClient{
    @Override
    public R removeAlyVideo(String videoId) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R removeBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
