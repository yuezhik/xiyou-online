package com.yuezhik.eduservice.service;

import com.yuezhik.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuezhik.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeByCourseId(String id);
}
