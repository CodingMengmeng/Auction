package com.example.auctionapp.wxapi.control;

import com.alibaba.fastjson.JSONObject;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.SuperControl;
import com.example.auctionapp.core.TokenModel;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.service.IWxPayService;
import com.example.auctionapp.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @Value("${wechat.mp.appid}")
    private String appId;
    @Value("${wechat.mp.appsecret}")
    private String secret;

    @Autowired
    private ICustomerService iCustomerService;

    @Autowired
    private IWxPayService iWxPayService;

    /**
     *
     * @return
     */
    @GetMapping("/wechatLogin")
    public Result wechatLogin(){
        //请求的地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize";

        Map<String,String> map = new HashMap<>(16);
        map.put("url",url);
        map.put("appid",appId);
        //请求获取code的回调地址
        //用线上环境的域名或者用内网穿透，不能用IP
        map.put("response_type","code");
        map.put("scope","snsapi_userinfo#wechat_redirect");
        return Result.success(map);
    }

    /**
     * 微信公众号 授权
     * @param
     * @return
     */
    @GetMapping("/wxCallBack")
    public Result wxCallBack(String code,String state) throws Exception{
        log.info("回调方法接收到的参数，param-->{},{}",code,state);
        if (StringUtils.isEmpty(code)){
            return Result.errorInfo("无code");
        }
        //获取access_token地址
        return iWxPayService.getUserinfo(code);
    }

    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
