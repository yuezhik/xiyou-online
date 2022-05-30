package com.yuezhik.educenter.controller;

import com.google.gson.Gson;
import com.yuezhik.commonutils.JwtUtils;
import com.yuezhik.educenter.entity.UcenterMember;
import com.yuezhik.educenter.service.UcenterMemberService;
import com.yuezhik.educenter.utils.ConstantWxUtils;
import com.yuezhik.educenter.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller //注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //生成微信扫描二维码,   %s 相当于?占位符
    @GetMapping("/login")
    public String getWxCode(){
        String baseUrl ="https://open.weixin.qq.com/connect/qrconnect"
                +"?appid=%s"
                +"&redirect_uri=%s"
                +"&response_type=code"
                +"&scope=snsapi_login"
                +"&state=%s"
                +"#wechat_redirect";

        //对redirect_uri进行URLEncoder编码
        String redirect_uri = ConstantWxUtils.WX_REDIRECT_URL;
        try {
            redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");//参数1：待编码字符串 参数2：编码方式
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设置 %s 占位符的参数，上面有3处
        String url = String.format(baseUrl,
                ConstantWxUtils.WX_APP_ID,
                redirect_uri,
                "yuezhik");


        //请求微信地址
        return "redirect:" + url;
    }
    @GetMapping("/callback")
    public String callback(String code, String state){
        //得到授权临时票据code
        //拿着code，去请求微信固定的地址，得到两个值 access_token 和 openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token"+
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        //拼接三个参数：id 秘钥 和 code值
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxUtils.WX_APP_ID
                , ConstantWxUtils.WX_APP_SECRET
                , code);


        //请求上面拼接好的地址，得到两个值 access_token 和 openid
        //使用httpclient【不用浏览器，也能模拟器出浏览器的请求和响应过程】发送请求，得到返回的结果
        String accessTokenInfo = null;
        try {
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo：。。。" +accessTokenInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
        String access_token = (String) mapAccessToken.get("access_token");
        String openid = (String) mapAccessToken.get("openid");

        //判断数据库是否存在相同的微信内容
        UcenterMember member = ucenterMemberService.getMemberByOpenId(openid);

        //如果没有，则为新用户，添加数据库
        if (member == null){

            //拿着 access_token 和 openid 的值再去请求微信提供的固定地址
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(resultUserInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, Object> resultMap = gson.fromJson(resultUserInfo, HashMap.class);

            String nickname = (String) resultMap.get("nickname");
            String headimgurl = (String) resultMap.get("headimgurl");

            //向数据库插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setOpenid(openid);
            ucenterMemberService.save(member);
        }

        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        //最后，返回首页，通过路径传递token字符串
        return "redirect:http://localhost:3000?token="+jwtToken;
    }

}


