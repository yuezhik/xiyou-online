package com.yuezhik.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduservice.entity.EduTeacher;
import com.yuezhik.eduservice.entity.vo.TeacherQuery;
import com.yuezhik.eduservice.service.EduTeacherService;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-03-26
 */
@RestController
@RequestMapping("/eduservice/teacher")
@Api(description = "讲师管理")
public class EduTeacherController {

    @Autowired   //http://localhost:8001/eduservice/teacher/getAll
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R list(){
//        try{
//            int i=10/0;
//        }catch (Exception e){
//            throw new GuliException(20001,"diy Exception");
//        }

        List<EduTeacher> list=teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/{id}")
    public R deleteTeacherById(@ApiParam(name = "id",value = "讲师ID",required = true)
                                   @PathVariable String id){
        boolean flag=teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/pageList/{page}/{limit}")
    public R pageList(@ApiParam(name = "page", value = "当前页码", required = true)@PathVariable Long page,
                      @ApiParam(name = "limit", value = "每页记录数", required = true)@PathVariable Long limit){
        Page<EduTeacher> pageParam=new Page<>(page,limit);

        //分页查询，查完后，会将数据封装在pageParam中
        teacherService.page(pageParam,null);
        //获取查询到的数据
        List<EduTeacher> records = pageParam.getRecords();
        //获取总记录数
        long total = pageParam.getTotal();

        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> pageParam=new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<EduTeacher>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end=teacherQuery.getEnd();

        if(!StringUtils.isEmpty(teacherQuery.getName())){
            wrapper.like("name",name);
        }if(!StringUtils.isEmpty(teacherQuery.getLevel())){
            wrapper.eq("level",level);
        }if(!StringUtils.isEmpty(teacherQuery.getBegin())){
            wrapper.ge("gmt_create",begin);
        }if(!StringUtils.isEmpty(teacherQuery.getEnd())){
            wrapper.le("gmt_modified",end);
        }

        teacherService.page(pageParam,wrapper);
        List<EduTeacher> records=pageParam.getRecords();
        long total=pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable String id){
        EduTeacher flag = teacherService.getById(id);
        return R.ok().data("item",flag);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.save(eduTeacher);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

}

