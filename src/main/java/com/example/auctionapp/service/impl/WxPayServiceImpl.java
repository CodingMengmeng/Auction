package com.example.auctionapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.core.*;
import com.example.auctionapp.dao.CustomerMapper;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.service.IWxPayService;
import com.example.auctionapp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.*;


/**
 * <p>
 * 拍品 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@Service
public class WxPayServiceImpl implements IWxPayService {

    @Value("${third.return.host}")
    private String host;

    //微信支付退款证书
    @Value("${wechat.cert.path}")
    private String wxCertPath;

    @Value("${wechat.mp.appid}")
    private String official_appId;

    @Value("${wechat.mp.appid}")
    private String appId;

    @Value("${wechat.mp.appsecret}")
    private String secret;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ICustomerService iCustomerService;
  /**
     *阿里APP统一支付接口
     * @param orderNumber
     * @param totalAmount
     * @return
     */
    @Override
    public AlipayTradeAppPayResponse wxpayTradeAppPay(String orderNumber, String totalAmount) throws AlipayApiException{
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ProjectConstant.APP_ID, ProjectConstant.APP_PRIVATE_KEY, "json", ProjectConstant.CHARSET, ProjectConstant.ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("华泰联合证券");
        model.setSubject("华泰联合证券");
        model.setOutTradeNo(orderNumber);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalAmount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setDisablePayChannels("creditCard,credit_group");//禁用信用卡渠道
        request.setBizModel(model);
        request.setReturnUrl(host+"/pay/alipay/gateway");
        request.setNotifyUrl(host+"/pay/alipay/notify");

        try {
            return alipayClient.sdkExecute(request);
        }catch (AlipayApiException e){
            throw e;
        }
    }

    @Override
    public AlipayTradeRefundResponse wxpayTradeRefund(String outTradeNo, String tradeNo, String refundAmount, String outRequestNo) throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "app_id", "your private_key", "json", "GBK", "alipay_public_key", "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        model.setRefundAmount(refundAmount);
        model.setRefundReason("正常退款");
        model.setOutRequestNo(outRequestNo);

        try {
            return alipayClient.sdkExecute(request);
        }catch (AlipayApiException e){
            throw e;
        }
    }

    /**
     * 微信退款申请接口
     * @param maps
     * @param transLog
     * @return
     */
    @Override
    public String wxpayTradeRefund(Map<String, String> maps, TransLog transLog) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("appid" , transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_APP.getType()) ? ProjectConstant.WECHAT_APPID : official_appId);
        map.put("mch_id" , ProjectConstant.MCH_ID);
        map.put("nonce_str", MD5Util.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes()));
        map.put("sign_type","MD5");
        map.put("transaction_id", maps.get("transaction_id"));
        map.put("out_refund_no", ComUtil.getOrderNumBer(transLog.getTransType(), transLog.getSubject()));
        //单位元  转化为单位分
        map.put("total_fee", transLog.getAmount().multiply(new BigDecimal(100)).toBigInteger());
        map.put("refund_fee", transLog.getPayAmount().multiply(new BigDecimal(100)).toBigInteger());
        //map.put("notify_url",host + "/pay/wxpay/notifyHandler");
        map.put("sign", addSign(map));

        StringBuffer xmlStr = new StringBuffer("<xml>");
        //map集合转换为xml集合
        XMLUtils.map2Element(map, xmlStr);
        xmlStr.append("</xml>");
        log.info("请求报文，param-->{}",xmlStr);

        //请求的URL
        String url="https://api.mch.weixin.qq.com/secapi/pay/refund";

        //请求的XML格式的字符串
        String xml=xmlStr.toString();

        // 申请退款 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
        KeyStore keyStore = null;
        FileInputStream instream=null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            instream = new FileInputStream(new File(wxCertPath));// P12文件目录
            keyStore.load(instream, ProjectConstant.MCH_ID.toCharArray());// 这里写密码..默认是你的MCHID
        }catch (Exception ex){
            log.error("#微信退款，加载证书异常 {}" , ex);
        }
         finally {
            try {
                if(instream !=null) {
                    instream.close();
                }
            }catch (IOException ioException){
                log.error("#微信退款，关闭io Stream发生异常 {}" , ioException);
            }
        }

        //1、创建一个HttpClient对象;
        CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault(keyStore,ProjectConstant.MCH_ID);
        CloseableHttpResponse httpResponse = null;
        //执行请求，返回的字符串
        String responseContent = null;

        //2、创建一个Http请求对象并设置请求的URL，比如 GET 请求就创建一个HttpGet 对象,POST请求就创建一个HttpPost对象；
        HttpRequestBase httpost = HttpRequestMethedEnum.HttpPost.createRequest(url);
        httpost.setConfig(HttpClientUtil.requestConfig);

        //3、如果需要可以设置请求对象的请求头参数，也可以往请求对象中添加请求参数；
        try {
            //设置微信需要的
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            if (xml != null){
                ((HttpEntityEnclosingRequest) httpost).setEntity(new StringEntity(xml ,ContentType.create("application/x-www-form-urlencoded","UTF-8")));
            }
            //4、调用HttpClient对象的execute方法执行请求；
            httpResponse = httpClient.execute(httpost);
            //5、获取请求响应对象和响应Entity；
            HttpEntity httpEntity = httpResponse.getEntity();
            //6、从响应对象中获取响应状态，从响应Entity中获取响应内容
            if (httpEntity != null){
                responseContent = EntityUtils.toString(httpEntity,"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //7、关闭响应对象
                if (httpResponse != null){
                    httpResponse.close();
                }
                //8、关闭HttpClient
                if (httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseContent;
    }

    /**
     * 构建微信支付统一下单请求报文
     * @param request
     * @param transLog
     * @return
     */
    @Override
    public Map<String, Object> createBillMessage(String orderNumBer,HttpServletRequest request, TransLog transLog) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("appid" , transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_APP.getType()) ? ProjectConstant.WECHAT_APPID : official_appId);
        map.put("mch_id" , ProjectConstant.MCH_ID);
        map.put("device_info" , "WEB");
        map.put("nonce_str", MD5Util.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes()));
        map.put("sign_type","MD5");
        map.put("body","七拍充值");
        map.put("out_trade_no", orderNumBer);
        map.put("fee_type","CNY");
        map.put("total_fee", StringUtils.getMoney(transLog.getPayAmount().toString()));
        map.put("spbill_create_ip",getIpAddress(request));
        map.put("notify_url",host + "/pay/wxpay/notifyHandler");
        if (transLog.getChildChannel().equals("3")){
            Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("id", transLog.getSubject()));
            map.put("openid",customer.getOpenId());
        }
        map.put("trade_type",transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_APP.getType())  ? "APP" : (transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_H5.getType())  ? "MWEB" : "JSAPI"));
        log.info("第一次加签，param --> {}",map);
        map.put("sign", addSign(map));

        //转化报文格式为XML
        StringBuffer xmlStr = new StringBuffer("<xml>");
        //map集合转换为xml集合
        XMLUtils.map2Element(map, xmlStr);
        xmlStr.append("</xml>");
        log.info("发送报文参数，param --> {}",xmlStr.toString());
        String reqs = HttpClientUtil.sendPostHttp("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlStr.toString());
        log.info("接收报文参数，param-->{}",reqs);

        //接收报文转化为Map集合
        Map mapList = XMLUtils.parseXmlToList(reqs);
        if (!"SUCCESS".equals(mapList.get("result_code"))){
            log.info("创单失败");
            map = new LinkedHashMap<>();
            map.put("package","FAIL");
            return map;
        }

        //验签
        if (examineSign(mapList,mapList.get("sign").toString())==false){
            return null;
        }
        //把接收到的东西重新封装返回给前端
        map = new LinkedHashMap<>();
        map.put("appid" , mapList.get("appid"));
        map.put("partnerid" , ProjectConstant.MCH_ID);
        map.put("prepayid" , mapList.get("prepay_id"));
        map.put("package" , "Sign=WXPay");
        map.put("noncestr",MD5Util.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes()));
        map.put("timestamp",System.currentTimeMillis()/1000);
        if (transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_H5.getType())){
            map.put("mweb_url" , mapList.get("mweb_url"));
        }
        map.put("sign",addSign(map));
        log.info("接收报文参数转化为重新封装返回报文，param-->{}",map);
        return map;
    }

    /**
     * 公众号登录时，获取用户授权信息
     * @param code
     * @return
     */
    @Override
    public Result getUserinfo(String code) {
        //获取access_token地址
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";

        //构建获取access_token报文 请求参数
        Map<String,String> reqMap = new HashMap<>(16);
        reqMap.put("appid",appId);
        reqMap.put("secret",secret);
        reqMap.put("code",code);
        reqMap.put("grant_type","authorization_code");
        log.info("构建获取access_token报文 请求参数,param-->{}",reqMap);

        //发送请求
        String result = HttpClientUtil.sendGetHttp(url,reqMap);
        log.info("获取 access_token 返回参数，param-->{}",result);

        //返回参数转为json
        JSONObject jsonObject = JSONObject.parseObject(result);

        //请求获取 userInfo 的地址
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo";

        Map<String,String> reqInfoMap = new HashMap<>(16);
        reqInfoMap.put("access_token",jsonObject.getString("access_token"));
        reqInfoMap.put("openid",jsonObject.getString("openid"));
        reqInfoMap.put("lang","zh_CN");
        log.info("构建获取 userInfo 报文 请求参数,param-->{}",reqMap);

        String resultInfo = HttpClientUtil.sendGetHttp(infoUrl, reqInfoMap);
        log.info("获取 userInfo 返回参数，param-->{}",resultInfo);

        JSONObject json2Object = JSONObject.parseObject(result);
        SuperControl superControl = iCustomerService.loginByUnionId(json2Object);
        if (superControl == null){
            Map<String,String> map = new HashMap<>();
            map.put("openId",json2Object.getString("openid"));
            map.put("name",json2Object.getString("nickname"));
            map.put("sex",json2Object.getString("sex"));
            map.put("unionId",json2Object.getString("unionid"));
            map.put("wxChannel","WPN");
            map.put("headPortrait",json2Object.getString("headimgurl"));
            return Result.success(map);
        }
        TokenModel tokenModel = new TokenModel(superControl.getNumber(),superControl.getObj());
        return Result.success(tokenModel);
    }

    /**
     * 获取手机IP地址
     * @param request
     * @return
     */
    public  String getIpAddress(HttpServletRequest request){
        String ipAddress = request.getRemoteAddr();
        if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ipAddress = inetAddress.getHostAddress();
        }
        if (ipAddress != null && ipAddress.length() > 15){
            if (ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0 , ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 生成待签名串
     * @param jsonObject
     * @return
     */
    public static String genSignData(JSONObject jsonObject){

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<>(jsonObject.keySet());
        Collections.sort(keys);
        for (int i = 0;i < keys.size(); i++){
            String key = keys.get(i);
            if ("sign".equals(key)){
                continue;
            }
            String value = jsonObject.getString(key);
            if (value == null || "".equals(value)){
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" +value);
        }
        String signStr = content.toString();
        if (signStr.startsWith("&")){
            signStr = signStr.replaceFirst("&","");
        }
        signStr += "&key="+ ProjectConstant.WECHAT_KAY;
        return signStr;
    }

    /**
     * 加签
     * @param map
     * @return
     */
    public static String addSign(Map map){
        String signData = genSignData(new JSONObject(map));
        log.info("第n次待签完毕，--------------------->{}",signData);
        String toUpperCase = MD5Util.MD5(signData).toUpperCase();

        return toUpperCase;
    }

    /**
     * 验签
     * @param map
     * @param before
     * @return
     */
    public static boolean examineSign(Map map,String before){
        map.remove("sign");
        log.info("验签，param --> {}",map);
        String signData = addSign(map);
        if (signData.equals(before)){
            return true;
        }
        return false;
    }
}
