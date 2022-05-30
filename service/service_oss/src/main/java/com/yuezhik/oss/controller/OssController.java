package com.yuezhik.oss.controller;

import com.yuezhik.commonutils.R;
import com.yuezhik.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
public class OssController {
    @Autowired
    private OssService ossService;

    @ApiOperation(value="文件上传")
    @PostMapping
    public R uploadOssFile(@RequestParam("file")MultipartFile file){
        String url=ossService.uploadFileAvatar(file);
        return R.ok().data("url",url).message("文件上传成功");
    }

}
