package com.yuezhik.eduorder.client;

import com.yuezhik.commonutils.UcenterMemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-ucenter")
public interface ServiceUcenterClient {
    @GetMapping("educenter/member/getUserInfoOrder/{memberId}")
    UcenterMemberVo getUserInfoOrder(@PathVariable("id") String memberId);
}
