package com.example.auctionapp.wxapi.vo;

/**
 * 授权码查询openid 返回对象
 *
 * @author zqy
 */
public class AuthCodeRespOpenId {

    private String return_code; //SUCCESS/FAIL
    private String return_msg; //返回信息，如非空，为错误原因  签名失败 参数格式校验错误

    /**
     * 以下字段在return_code为SUCCESS的时候有返回
     */
    private String appid; //微信分配的公众账号ID
    private String mch_id;//微信支付分配的商户号
    private String nonce_str;//随机字符串，不长于32位
    private String sign;
    private String result_code; // SUCCESS/FAIL

    /**
     * SYSTEMERROR--系统错误
     * AUTHCODEEXPIRE---授权码过期
     * AUTH_CODE_ERROR—授权码错误
     * AUTH_CODE_INVALID—授权码检验错误
     */
    private String err_code;

    /**
     * 以下字段在return_code 和result_code都为SUCCESS的时候有返回
     */
    private String openid; //是 String(128) 用户在商户appid下的唯一标识

    public String getReturn_code() {
        return return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
