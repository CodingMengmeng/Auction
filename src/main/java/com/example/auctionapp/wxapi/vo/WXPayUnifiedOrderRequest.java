package com.example.auctionapp.wxapi.vo;

import java.io.Serializable;

/**
 * 微信支付"统一下单"请求参数
 * 
 * @author pucker
 *
 */
public class WXPayUnifiedOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 公众账号ID
	 */
	private String appid;
	/**
	 * 商户号
	 */
	private String mch_id;
	/**
	 * 设备号
	 */
	private String device_info;
	/**
	 * 随机字符串
	 */
	private String nonce_str;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 附加数据
	 */
	private String attach;
	/**
	 * 商户订单号
	 */
	private String out_trade_no;
	/**
	 * 总金额
	 */
	private String total_fee;
	/**
	 * 终端IP
	 */
	private String spbill_create_ip;
	/**
	 * 通知地址
	 */
	private String notify_url;
	/**
	 * 交易类型
	 */
	private String trade_type;
	/**
	 * 指定支付方式(no_credit--指定不能使用信用卡支付)
	 */
	private String limit_pay;
	/**
	 * 用户标识
	 */
	private String openid;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
