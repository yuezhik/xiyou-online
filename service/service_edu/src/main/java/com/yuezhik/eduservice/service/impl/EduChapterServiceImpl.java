package com.yuezhik.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuezhik.eduservice.entity.EduChapter;
import com.yuezhik.eduservice.entity.EduVideo;
import com.yuezhik.eduservice.entity.chapter.ChapterVo;
import com.yuezhik.eduservice.entity.chapter.VideoVo;
import com.yuezhik.eduservice.mapper.EduChapterMapper;
import com.yuezhik.eduservice.service.EduChapterService;
import com.yuezhik.eduservice.service.EduVideoService;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-12
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> chapters = baseMapper.selectList(wrapper);

        QueryWrapper<EduVideo> videoWrapper=new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        List<EduVideo> videos = videoService.list(videoWrapper);

        List<ChapterVo> chapterVos=new ArrayList<>();
        for (EduChapter chapter : chapters) {
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            List<VideoVo> children=new ArrayList<>();
            for (EduVideo video : videos) {
                if(video.getChapterId().equals(chapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    children.add(videoVo);
                }
            }
            chapterVo.setChildren(children);
            chapterVos.add(chapterVo);
        }
        return chapterVos;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if(count>0){
            throw new GuliException(20001,"不能删除！！有小节内容！");
        }else{
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }

    @Override
    public void removeByCourseId(String id) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }
}
