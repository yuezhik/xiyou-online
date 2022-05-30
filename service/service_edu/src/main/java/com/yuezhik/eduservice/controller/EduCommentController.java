package com.yuezhik.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.commonutils.R;
import com.yuezhik.commonutils.UcenterMemberVo;
import com.yuezhik.eduservice.client.MemberClient;
import com.yuezhik.eduservice.entity.EduComment;
import com.yuezhik.eduservice.service.EduCommentService;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/eduservice/comment")
public class EduCommentController {
    @Autowired
    private EduCommentService commentService;
    @Autowired
    private MemberClient memberClient;

    @GetMapping("/getCommentPage/{page}/{limit}")
    public R getCommentPage(@PathVariable Long page,@PathVariable Long limit,String courseId){
        Page<EduComment> commentPage=new Page<>(page,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseId)){
            wrapper.eq("course_id",courseId);
        }
        wrapper.orderByDesc("gmt_create");
        commentService.page(commentPage,wrapper);
        List<EduComment> commentList = commentPage.getRecords();
        long current = commentPage.getCurrent();//当前页
        long size = commentPage.getSize();//一页记录数
        long total = commentPage.getTotal();//总记录数
        long pages = commentPage.getPages();//总页数
        boolean hasPrevious = commentPage.hasPrevious();//是否有上页
        boolean hasNext = commentPage.hasNext();//是否有下页

        HashMap<String, Object> map = new HashMap<>();
        map.put("current",current);
        map.put("size",size);
        map.put("total",total);
        map.put("pages",pages);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);
        map.put("list",commentList);

        return R.ok().data(map);
    }
    //添加评论
    @PostMapping("/addComment")
    public R addComment(HttpServletRequest request, @RequestBody EduComment eduComment){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //判断用户是否登录
        if (StringUtils.isEmpty(memberId)){
            throw new GuliException(20001,"请先登录");
        }
        eduComment.setMemberId(memberId);

        //远程调用ucenter根据用户id获取用户信息
        UcenterMemberVo memberVo = memberClient.getUserInfoOrder(memberId);

        eduComment.setAvatar(memberVo.getAvatar());
        eduComment.setNickname(memberVo.getNickname());

        //保存评论
        commentService.save(eduComment);

        return R.ok();
    }

}

