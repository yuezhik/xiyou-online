package com.yuezhik.eduservice.service;

import com.yuezhik.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String id);
}
