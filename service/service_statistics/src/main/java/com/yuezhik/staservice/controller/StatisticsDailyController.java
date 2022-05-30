package com.yuezhik.staservice.controller;


import com.yuezhik.commonutils.R;
import com.yuezhik.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-13
 */
@RestController
@RequestMapping("/staservice/sta")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;

    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable String day){
        dailyService.registerCount(day);
        return R.ok();
    }
    //图表显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end){
        Map<String,Object> map=dailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }
}

