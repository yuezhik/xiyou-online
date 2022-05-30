package com.yuezhik.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.eduservice.entity.EduSubject;
import com.yuezhik.eduservice.entity.excel.SubjectData;
import com.yuezhik.eduservice.entity.subject.OneSubject;
import com.yuezhik.eduservice.entity.subject.TwoSubject;
import com.yuezhik.eduservice.listener.SubjectExcelListener;
import com.yuezhik.eduservice.mapper.EduSubjectMapper;
import com.yuezhik.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-11
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try{
            InputStream is=file.getInputStream();

            EasyExcel.read(is, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //一级分类
        QueryWrapper<EduSubject> oneWrapper=new QueryWrapper<>();
        oneWrapper.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(oneWrapper);
        //二级分类
        QueryWrapper<EduSubject> twoWrapper=new QueryWrapper<>();
        twoWrapper.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(twoWrapper);
        List<OneSubject> finalSubjectList=new ArrayList<>();
        //对一级二级数据进行封装
        for (EduSubject sub :
                oneSubjectList) {
            OneSubject oneSubject=new OneSubject();
            BeanUtils.copyProperties(sub,oneSubject);
            List<TwoSubject> twoSubjects=new ArrayList<>();
            for (EduSubject subject :
                    twoSubjectList) {
                if(subject.getParentId().equals(sub.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(subject, twoSubject);
                    twoSubjects.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjects);
            finalSubjectList.add(oneSubject);
        }
        return finalSubjectList;
    }
}
