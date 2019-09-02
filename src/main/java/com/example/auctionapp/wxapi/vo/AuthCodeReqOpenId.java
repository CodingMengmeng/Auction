package com.example.auctionapp.wxapi.vo;

/**
 * 授权码查询openid 请求对象
 *
 * @author zqy
 */
public class AuthCodeReqOpenId {

    private String appid;  //微信分配的公众账号ID
    private String mch_id; //微信支付分配的商户号
    private String auth_code;  //扫码支付授权码，设备读取用户微信中的条码或者二维码信息
    private String nonce_str;  //随机字符串，不长于32位
    private String sign;       //签名


    public String getAppid() {
        return appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
