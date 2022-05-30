package com.yuezhik.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.commonutils.R;
import com.yuezhik.eduorder.entity.TOrder;
import com.yuezhik.eduorder.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/eduorder/order")
public class TOrderController {
    @Autowired
    private TOrderService orderService;

    @PostMapping("/createOrder")
    //创建订单
    public R createOrder(@RequestBody TOrder order, HttpServletRequest request){

        String orderNo=orderService.createOrders(order.getCourseId(), JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }
    @GetMapping("/getOrderInfo/{orderId}")
    //获取订单信息
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<TOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("item",order);
    }
    //根据课程id和用户id查询订单的支付状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public Boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId){
        QueryWrapper<TOrder> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);

        int count = orderService.count(wrapper);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}

