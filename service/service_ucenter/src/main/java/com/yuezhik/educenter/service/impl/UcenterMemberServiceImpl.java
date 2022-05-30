package com.yuezhik.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.educenter.utils.MD5;
import com.yuezhik.educenter.entity.UcenterMember;
import com.yuezhik.educenter.entity.vo.LoginVo;
import com.yuezhik.educenter.entity.vo.RegisterVo;
import com.yuezhik.educenter.mapper.UcenterMemberMapper;
import com.yuezhik.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-30
 */

@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //登录的方法
    @Override
    public String login(LoginVo loginVo) {
        //获取手机号和密码
        String phone = loginVo.getMobile();
        String password = loginVo.getPassword();
        //判断输入的手机号和密码是否为空
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(phone)){
            throw new GuliException(20001,"手机号或密码为空");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",phone);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember == null){
            throw new GuliException(20001,"手机号不存在");
        }

        //判断密码是否正确
        // MD5加密是不可逆性的，不能解密，只能加密
        //将获取到的密码经过MD5加密与数据库比较
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"密码不正确");
        }

        //判断用户是否禁用
        if (ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"用户被禁用");
        }

        //生成jwtToken
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return token;

    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)||StringUtils.isEmpty(nickname)){
            throw new GuliException(20001,"注册失败");
        }
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(StringUtils.isEmpty(redisCode)){
            throw new GuliException(20001,"验证码已过期");
        }
        if(!redisCode.equals(code)){
            throw new GuliException(20001,"验证码错误");
        }
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"该账号已注册");
        }
        UcenterMember member=new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setIsDisabled(false);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("http://www.weixintouxiang.cn/weixin/20140607090832328.gif");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getMemberByOpenId(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer getRegisterCount(String day) {
        return baseMapper.getRegisterCount(day);
    }
}
