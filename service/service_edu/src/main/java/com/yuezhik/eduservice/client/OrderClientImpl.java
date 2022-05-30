package com.yuezhik.eduservice.client;

import com.yuezhik.servicebase.config.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient{
    @Override
    public Boolean isBuyCourse(String courseId, String memberId) {
        throw new GuliException(20001,"查询错误");
    }
}
