package com.example.auctionapp.wxapi.process;


import com.example.auctionapp.wxapi.util.CalendarUtil;

/**
 * 接口凭证
 */
public class JSTicket {
	private String ticket;// 接口访问凭证
	private int expiresIn;// 凭证有效期，单位：秒
	private long createTime;// 创建时间，单位：秒 ，用于判断是否过期

	private Integer errcode;// 错误编码
	private String errmsg;// 错误消息

	/** 业务逻辑处理字段 **/
	private boolean expires = false;// 是否过期

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
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
		this.errmsg = ErrCode.errMsg(errcode);
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

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