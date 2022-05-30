package com.yuezhik.eduorder.service.impl;

import com.yuezhik.commonutils.EduCourseVo;
import com.yuezhik.commonutils.UcenterMemberVo;
import com.yuezhik.eduorder.client.ServiceEduClient;
import com.yuezhik.eduorder.client.ServiceUcenterClient;
import com.yuezhik.eduorder.entity.TOrder;
import com.yuezhik.eduorder.mapper.TOrderMapper;
import com.yuezhik.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuezhik.eduorder.utils.OrderNoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-05-12
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private ServiceEduClient eduClient;
    @Autowired
    private ServiceUcenterClient ucenterClient;

    @Override
    public String createOrders(String courseId, String memberId) {
        EduCourseVo courseInfo = eduClient.getCourseInfoOrder(courseId);
        UcenterMemberVo memberInfo = ucenterClient.getUserInfoOrder(memberId);

        TOrder tOrder = new TOrder();
        tOrder.setMobile(memberInfo.getMobile());
        tOrder.setNickname(memberInfo.getNickname());
        tOrder.setMemberId(memberId);
        tOrder.setCourseCover(courseInfo.getCover());
        tOrder.setCourseId(courseId);
        tOrder.setCourseTitle(courseInfo.getTitle());
        tOrder.setTeacherName(courseInfo.getTeacherName());
        tOrder.setTotalFee(courseInfo.getPrice());
        tOrder.setStatus(0);//支付状态：（ 0：已支付，1：未支付 ）
        tOrder.setPayType(1);//支付类型： 1：微信 ， 2：支付宝
        tOrder.setOrderNo(OrderNoUtil.getOrderNo()); //订单号

        //保存订单
        baseMapper.insert(tOrder);
        return tOrder.getOrderNo();
    }
}
