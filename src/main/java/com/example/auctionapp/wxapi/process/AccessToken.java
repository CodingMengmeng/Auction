package com.example.auctionapp.wxapi.process;


import com.example.auctionapp.wxapi.util.CalendarUtil;

public class AccessToken {
	private String accessToken;// 接口访问凭证
	private int expiresIn;// 凭证有效期，单位：秒
	private long createTime;// 创建时间，单位：秒 ，用于判断是否过期

	private Integer errcode;// 错误编码
	private String errmsg;// 错误消息

	/** 业务逻辑处理字段 **/
	private boolean expires = false;// 是否过期

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	/** 业务逻辑处理字段 **/
	public void setExpires(boolean expires) {
		this.expires = expires;
	}

	public boolean getExpires() {
		return expires;
	}

	public boolean isExpires() {
		long now = CalendarUtil.getTimeInSeconds();
		return now - this.createTime - 10 >= this.expiresIn; // 预留 10s
	}
}