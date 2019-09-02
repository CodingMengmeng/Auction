package com.example.auctionapp.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 实名验证
 * api链接：https://market.aliyun.com/products/57000002/cmapi021096.html#sku=yuncode1509600000
 * AppKey：25092754
 * AppSecret：68b9a55c72448160932a60108066e1f9
 * AppCode：5d8aaa6d3a6f4c0ba9c310406cad528a
 *
 * @author 孔邹祥
 * @date 2019年3月11日18:13:59
 */
public class BankUtils {


    /**
     * 验证银行卡是否正确
     *
     * @param cardNo  银行卡号
     * @param idNo    身份证号
     * @param name    身份证对应的名称
     * @param phoneNo 手机号
     * @return 银行卡的详细信息
     * @throws Exception 异常
     */
    public static String bankAuthenticate(String cardNo, String idNo, String name, String phoneNo)  {

        try {

        String host = "https://yunyidata.market.alicloudapi.com";
        String path = "/bankAuthenticate4";
        String method = "POST";
        String appcode = "5d8aaa6d3a6f4c0ba9c310406cad528a";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("ReturnBankInfo", "YES");
        bodys.put("cardNo", cardNo);
        bodys.put("idNo", idNo);
        bodys.put("name", name);
        bodys.put("phoneNo", phoneNo);

        /*
         * 重要提示如下:
         * HttpUtils请从
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
         * 下载
         *
         * 相应的依赖请参照
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
         */
        HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);

        //返回response的body

            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
