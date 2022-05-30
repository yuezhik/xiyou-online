package com.yuezhik.eduorder.service;

import com.yuezhik.eduorder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-12
 */
public interface TOrderService extends IService<TOrder> {

    String createOrders(String courseId, String memberId);
}
