package com.yuezhik.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.eduservice.client.VodClient;
import com.yuezhik.eduservice.entity.EduVideo;
import com.yuezhik.eduservice.mapper.EduVideoMapper;
import com.yuezhik.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;
    //根据课程id删除小节
    @Override
    public void removeByCourseId(String id) {

        //根据课程id查询课程里面的所有视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);

        List<String> list = new ArrayList<>();
        for (EduVideo eduVideo : eduVideos) {
            String sourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(sourceId)){
                list.add(sourceId);
            }

        }

        //根据多个视频id，删除多个视频
        if (list.size()>0){
            vodClient.removeBatch(list);
        }

        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id", id);
        baseMapper.delete(queryWrapper2);

    }

}
