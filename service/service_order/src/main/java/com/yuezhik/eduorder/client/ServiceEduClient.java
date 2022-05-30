package com.yuezhik.eduorder.client;

import com.yuezhik.commonutils.EduCourseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="service-edu")
public interface ServiceEduClient {
    @PostMapping("/eduservice/courseFront/getCourseInfoOrder/{courseId}")
    EduCourseVo getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
