package com.yuezhik.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.Test;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws ClientException {
        //1、根据视频id获取视频播放地址
        //创建初始化对象
        DefaultAcsClient cl = InitObject.initVodClient("LTAI5tCdLWzNJavXxTpUZCcc", "W3UiAWMQWtqIAtGTv6W9HNDeZghjsd");
        //创建获取视频地址request对象和response对象
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        //向request对象设置视频id值
        request.setVideoId("e20df101884440dd963a0fd1c6e5b0a5");


        //调用初始化对象里面的方法传递request，获取数据
        response = cl.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            //PlayInfo.PlayURL = https://outin-5e688ec47b2c11ebb00500163e06123c.oss-cn-shanghai.aliyuncs.com/sv/52a57879-177f1fd5033/52a57879-177f1fd5033.mp4?Expires=1614680252&OSSAccessKeyId=LTAIrkwb21KyGjJl&Signature=FPlVfGbIDVGuvR3W8f2K4QcpATw%3D
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");//VideoBase.Title = 6 - What If I Want to Move Faster.mp4
    }
    @Test
    public void testFileUpload(){
        String accessKeyId = "LTAI5tCdLWzNJavXxTpUZCcc";
        String accessKeySecret = "W3UiAWMQWtqIAtGTv6W9HNDeZghjsd";
        String title = "Aotm"; //上传之后文件的名称
        String fileName = "C:\\Users\\86177\\Desktop\\视频点播\\01.mp4"; //本地文件的路径和名称

        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n"); //获取到上传视频的id
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    @Test
    public void test1() throws ClientException {
        //创建初始化对象
        DefaultAcsClient cl = InitObject.initVodClient("LTAI5tCdLWzNJavXxTpUZCcc", "W3UiAWMQWtqIAtGTv6W9HNDeZghjsd");
        //创建获取视频地址request对象和response对象
        GetVideoPlayAuthRequest request =new GetVideoPlayAuthRequest();
        //向request对象设置视频id值
        request.setVideoId("e20df101884440dd963a0fd1c6e5b0a5");

        GetVideoPlayAuthResponse response = cl.getAcsResponse(request);

        //播放凭证
        System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
        //VideoMeta信息
        System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");

    }


}
