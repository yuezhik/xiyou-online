package com.yuezhik.educenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Component

public class UcenterConfig {
    //https://www.cnblogs.com/scocraff/p/15795276.html
    //因为重定向的视图资源并没有被正确解析,配置Spring自带的内部资源解析器
    @Bean
    public InternalResourceViewResolver viewResolver(){

        return new InternalResourceViewResolver();

    }
}
