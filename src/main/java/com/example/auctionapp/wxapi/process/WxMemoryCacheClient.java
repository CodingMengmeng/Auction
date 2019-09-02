package com.example.auctionapp.wxapi.process;

import com.example.auctionapp.wxapi.util.CalendarUtil;
import com.example.auctionapp.wxapi.util.WxConstants;
import com.example.auctionapp.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类； 目前使用 服务器内存的方式；
 * <p>
 * 1、开发者可以根据自己的需求使用不同的缓存方式,比如memcached 2、系统默认使用单个公众账号的缓存处理，如果有多个账号，请开发者自行处理
 */
@Slf4j
@Configuration
public class WxMemoryCacheClient {

    @Autowired
    private IRedisService redisService;

    // 服务器内存的方式缓存account、accessToken、jsTicket
    private static Map<String, MpAccount> mpAccountMap = new HashMap<String, MpAccount>();

    private static Map<String, AccessToken> accountAccessTokenMap = new HashMap<String, AccessToken>();
    public final static String ACCOUNT_ACCESS_TOKENKEY = "ACCOUNT_ACCESS_TOKEN_KEY_";

    private static Map<String, JSTicket> accountJSTicketMap = new HashMap<String, JSTicket>();
    public final static String JS_TICKET_KEY = "JS_TICKET_KEY_";

    // 微信OAuth认证的时候，服务器内存的方式缓存openid; key=sessionid ，value=openid
    // private static Map<String,String> sessionOpenIdMap = new
    // HashMap<String,String>();
    private static Map<String, OAuthAccessToken> accountOAuthTokenMap = new HashMap<String, OAuthAccessToken>();
    public final static String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY_";

    public static void addMpAccount(MpAccount account) {
        if (account != null && !mpAccountMap.containsKey(account.getAccount())) {
            mpAccountMap.put(account.getAccount(), account);
        }
    }

    public  MpAccount getMpAccount(String account) {
        return mpAccountMap.get(account);
    }

    // 获取唯一的公众号,如果需要多账号，请自行处理
    public  MpAccount getSingleMpAccount() {
        MpAccount sigleAccount = null;
        for (String key : mpAccountMap.keySet()) {
            sigleAccount = mpAccountMap.get(key);
            break;
        }
        return sigleAccount;
    }

    public AccessToken addAccessToken(String account, AccessToken token) {
        if (token != null) {
            token.setCreateTime(CalendarUtil.getTimeInSeconds());
            redisService.set(ACCOUNT_ACCESS_TOKENKEY + account, JSONObject.fromObject(token).toString(),3L,TimeUnit.HOURS);
        } else {
            accountAccessTokenMap.put(account, token);
        }
        return token;
    }

    /**
     * accessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @param account
     * @return
     */
    public AccessToken getAccessToken(String account) {
        return accountAccessTokenMap.get(account);
    }

    /**
     * 获取唯一的公众号的accessToken,如果需要多账号，请自行处理
     * accessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @return
     */
    public AccessToken getSingleAccessToken(String account) {
        String jsonStr = String.valueOf(redisService.get(ACCOUNT_ACCESS_TOKENKEY + account));
        return (AccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), AccessToken.class);
    }

    /**
     * 添加JSTicket到缓存
     *
     * @param account
     * @param jsTicket
     * @return
     */
    public JSTicket addJSTicket(String account, JSTicket jsTicket) {
        if (jsTicket != null) {
            jsTicket.setCreateTime(CalendarUtil.getTimeInSeconds());
            redisService.set(JS_TICKET_KEY + account, JSONObject.fromObject(jsTicket).toString());
        }
        return jsTicket;
    }

    /**
     * JSTicket的获取，绝对不要从缓存中直接获取，请从JSTicket中获取；
     *
     * @param account
     * @return
     */
    public JSTicket getJSTicket(String account) {
        String jsonStr = String.valueOf(redisService.get(JS_TICKET_KEY + account));
        return (JSTicket) JSONObject.toBean(JSONObject.fromObject(jsonStr), JSTicket.class);
    }

    /**
     * 获取唯一的公众号的JSTicket,如果需要多账号，请自行处理
     * JSTicket的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @return
     */
    public JSTicket getSingleJSTicket(String account) {
        String jsonStr = String.valueOf(redisService.get(JS_TICKET_KEY + account));
        return (JSTicket) JSONObject.toBean(JSONObject.fromObject(jsonStr), JSTicket.class);
    }

    // //处理openid缓存
    // public static String getOpenid(String sessionid){
    // if(!StringUtils.isBlank(sessionid)){
    // return sessionOpenIdMap.get(sessionid);
    // }
    // return null;
    // }

    // 处理openid缓存
    public String getOpenid(HttpServletRequest request) {
        Object openId = request.getSession().getAttribute(WxConstants.OPENID_SESSION_KEY);
        if (openId != null) {
            return String.valueOf(openId);
        }
        return "";
    }

    // public static String setOpenid(String sessionid, String openid){
    // if(!StringUtils.isBlank(sessionid) && !StringUtils.isBlank(openid)){
    // sessionOpenIdMap.put(sessionid, openid);
    // }
    // return openid;
    // }

    public static String setOpenid(HttpServletRequest request, String openid) {
        request.getSession().setAttribute(WxConstants.OPENID_SESSION_KEY, openid);
        return openid;
    }

    // 处理OAuth的Token
    public AccessToken addOAuthAccessToken(String account, OAuthAccessToken token) {
        if (token != null) {
            redisService.set(ACCESS_TOKEN_KEY + account, JSONObject.fromObject(token).toString(),3L,TimeUnit.HOURS);
        }
        return token;
    }

    /**
     * OAuthAccessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @param account
     * @return
     */
    public OAuthAccessToken getOAuthAccessToken(String account) {
        String jsonStr = String.valueOf(redisService.get(ACCESS_TOKEN_KEY + account));
        return (OAuthAccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), OAuthAccessToken.class);
    }

    /**
     * 获取唯一的公众号的accessToken,如果需要多账号，请自行处理
     * OAuthAccessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @return
     */
    public OAuthAccessToken getSingleOAuthAccessToken(String account) {
        String jsonStr = String.valueOf( redisService.get(ACCESS_TOKEN_KEY + account));
        return (OAuthAccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), OAuthAccessToken.class);

    }

    /**
     * 获取所有的公众号号 All MpAccount的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
     *
     * @return
     */
    public  List<MpAccount> getAllMpAccountMaps() {
        Collection<MpAccount> valueCollection = mpAccountMap.values();
        List<MpAccount> list = new ArrayList<MpAccount>(valueCollection);
        return list;
    }
}
