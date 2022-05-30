package com.yuezhik.educms.controller;

import com.yuezhik.commonutils.R;
import com.yuezhik.educms.entity.CrmBanner;
import com.yuezhik.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("/getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> banner=bannerService.selectAllBanner();
        return R.ok().data("list",banner);
    }

}
