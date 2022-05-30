package com.yuezhik.eduorder.controller;


import com.yuezhik.commonutils.R;
import com.yuezhik.eduorder.service.TPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/eduorder/paylog")
public class TPayLogController {
    @Autowired
    private TPayLogService payLogService;

    //生成微信支付二维码接口
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //返回信息，包含二维码地址，还有其他需要的信息
        Map map=payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    //根据订单号查询订单支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){

        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        if (map==null){
            return R.error().message("支付出错了");
        }
        //如果返回的map不为空，通过map获取订单的状态
        if (map.get("trade_state").equals("SUCCESS")){ //支付成功
            //添加记录到支付表里，并更新订单表的状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().message("支付中");
    }

}

