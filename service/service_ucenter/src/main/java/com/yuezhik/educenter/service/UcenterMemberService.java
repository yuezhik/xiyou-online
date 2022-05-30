package com.yuezhik.educenter.service;

import com.yuezhik.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuezhik.educenter.entity.vo.LoginVo;
import com.yuezhik.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-30
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    UcenterMember getMemberByOpenId(String openid);

    Integer getRegisterCount(String day);
}
