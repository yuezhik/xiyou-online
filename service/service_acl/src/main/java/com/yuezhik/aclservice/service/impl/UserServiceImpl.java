package com.yuezhik.aclservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuezhik.aclservice.entity.User;
import com.yuezhik.aclservice.entity.UserRole;
import com.yuezhik.aclservice.mapper.UserMapper;
import com.yuezhik.aclservice.service.UserRoleService;
import com.yuezhik.aclservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService roleService;

    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public void removeUserId(String id) {
        QueryWrapper<UserRole> userWrapper=new QueryWrapper<>();
        userWrapper.eq("user_id",id);
        roleService.remove(userWrapper);
        baseMapper.deleteById(id);
    }
}
