package com.yuezhik.staservice.schedule;

import com.yuezhik.staservice.service.StatisticsDailyService;
import com.yuezhik.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService dailyService;

    //在每天凌晨1点执行方法，把前一天的数据查询进行添加
    @Scheduled(cron = "0 0 1 * * ? ")//指定cron表达式规则
    public void task02(){
        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
