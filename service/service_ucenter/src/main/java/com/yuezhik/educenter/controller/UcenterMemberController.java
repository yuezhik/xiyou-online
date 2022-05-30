package com.yuezhik.educenter.controller;


import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.commonutils.R;
import com.yuezhik.commonutils.UcenterMemberVo;
import com.yuezhik.educenter.entity.UcenterMember;
import com.yuezhik.educenter.entity.vo.LoginVo;
import com.yuezhik.educenter.entity.vo.RegisterVo;
import com.yuezhik.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("/login")
    public R loginUser(@RequestBody LoginVo loginVo){
        //返回token，使用jwt生成
        String token = memberService.login(loginVo);
        return R.ok().data("token",token);
    }
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }
    @PostMapping("/getUserInfoOrder/{memberId}")
    public UcenterMemberVo getUserInfoOrder(@PathVariable String memberId){
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberVo memberVo = new UcenterMemberVo();
        BeanUtils.copyProperties(member,memberVo);

        return memberVo;
    }
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count=memberService.getRegisterCount(day);
        return R.ok().data("countRegister",count);
    }
}

