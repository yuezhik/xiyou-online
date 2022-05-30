package com.yuezhik.staservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.commonutils.R;
import com.yuezhik.staservice.client.UcenterClient;
import com.yuezhik.staservice.entity.StatisticsDaily;
import com.yuezhik.staservice.mapper.StatisticsDailyMapper;
import com.yuezhik.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-13
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public void registerCount(String day) {
        //添加之前先删除表相同的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);


        //远程调用得到某一天的注册人数
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) registerR.getData().get("countRegister");

        //其他的数据类似，也是通过远程调用，获取数据返回即可，下面使用随机数模拟

        //把获取到的数据
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(countRegister);//注册人数
        daily.setCourseNum(RandomUtils.nextInt(100,200));
        daily.setLoginNum(RandomUtils.nextInt(200,300));//登录数
        daily.setVideoViewNum(RandomUtils.nextInt(200,300));//视频流量数
        daily.setDateCalculated(day);//统计日期

        //添加到数据库中
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.select("date_calculated",type);
        wrapper.between("date_calculated",begin,end);

        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);

        //前端要求数组json结果，对应后端为List集合
        //创建两个list集合，一个放日期X轴，一个放数量Y轴
        List<String> xlist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();

        for (StatisticsDaily staDaily : dailyList) {
            xlist.add(staDaily.getDateCalculated());
            if("register_num".equals(type)){
                ylist.add(staDaily.getRegisterNum());
            }
            if ("login_num".equals(type)){
                ylist.add(staDaily.getLoginNum());
            }
            if ("video_view_num".equals(type)){
                ylist.add(staDaily.getVideoViewNum());
            }
            if ("course_num".equals(type)){
                ylist.add(staDaily.getCourseNum());
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("date_calculatedList",xlist);
        map.put("numDataList",ylist);

        return map;
    }
}
