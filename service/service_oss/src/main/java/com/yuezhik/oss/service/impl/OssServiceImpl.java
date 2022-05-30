package com.yuezhik.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.yuezhik.oss.service.OssService;
import com.yuezhik.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        InputStream inputStream = null;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件的输入流
            inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();
            String uuid=UUID.randomUUID().toString().replaceAll("-","");
            fileName=uuid+fileName;

            String datePath=new DateTime().toString("yyyy/MM/dd");

            fileName=datePath+"/"+fileName;

            //调用oss实例中的方法实现上传
            //参数1： Bucket名称
            //参数2： 上传到oss文件路径和文件名称 /aa/bb/1.jpg
            //参数3： 上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://edu-yuezhik1010.oss-cn-beijing.aliyuncs.com/default.gif
            String url = "http://"+bucketName+"."+endpoint+"/"+fileName ;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
