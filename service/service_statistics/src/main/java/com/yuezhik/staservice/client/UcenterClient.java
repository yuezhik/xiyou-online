package com.yuezhik.staservice.client;

import com.yuezhik.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter")
public interface UcenterClient {
    @GetMapping("educenter/member/countRegister/{day}")
    R countRegister(@PathVariable("day") String day);
}
