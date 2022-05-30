package com.yuezhik.educenter.mapper;

import com.yuezhik.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-30
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer getRegisterCount(String day);
}
