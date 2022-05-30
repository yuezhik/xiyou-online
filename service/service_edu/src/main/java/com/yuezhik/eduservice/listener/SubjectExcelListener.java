package com.yuezhik.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.eduservice.entity.EduSubject;
import com.yuezhik.eduservice.entity.excel.SubjectData;
import com.yuezhik.eduservice.service.EduSubjectService;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.ehcache.shadow.org.terracotta.offheapstore.util.DebuggingUtils;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectExcelListener態交给spring进行ioc管理，需要自己手动new，不能注入其他对象
    //不能实现数据库操作

    public EduSubjectService eduSubjectService;
    //有参，传递subjectService用于操作数据库
    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //无参
    public SubjectExcelListener() {
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData==null){
            throw new GuliException(20001,"添加失败");
        }
        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断是否有一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if(existOneSubject==null){
            existOneSubject=new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(existOneSubject);
        }
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), existOneSubject.getId());
        if(existTwoSubject==null){
            existTwoSubject=new EduSubject();
            existTwoSubject.setParentId(existOneSubject.getId());
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(existTwoSubject);
        }
    }
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name).eq("parent_id","0");
        EduSubject oneSubject = eduSubjectService.getOne(wrapper);
        return oneSubject;
    }
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String parentId){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name).eq("parent_id",parentId);
        EduSubject twoSubject = eduSubjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
