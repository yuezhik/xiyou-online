package com.yuezhik.eduservice.controller;

import com.yuezhik.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@Api(description = "登录管理")
public class EduLoginControlller {
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }
    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","http://www.weixintouxiang.cn/weixin/20140607090832328.gif");
    }
}
