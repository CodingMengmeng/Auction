package com.example.auctionapp.wxapi.domain;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 账号粉丝用户信息
 * 
 */
public class AccountFans implements java.io.Serializable {

	private static final long serialVersionUID = 4842171198462346877L;
	private String id;
	private String openId;// openId，每个用户都是唯一的
	private Integer subscribestatus;// 订阅状态
	private String subscribeTime;// 订阅时间
	private byte[] nickname;// 昵称,二进制保存emoji表情
	private String nicknameStr;// 昵称显示
	private String wxid;// 微信号
	private Integer gender;// 性别 0-女；1-男；2-未知
	private String language;// 语言
	private String country;// 国家
	private String province;// 省
	private String city;// 城市
	private String headimgurl;// 头像
	private String remark;// 备注
	private int status;// 用户状态 1-可用；0-不可用

	private String unionid;// 联合ID
	private String groupid; // 所属组

	/**
	 * 用户操作菜单状态 00-无权限；10-获得权限 ...
	 */
	private String fansStatus;// 2016-7-13 add by pucker
	private Date createtime = new Date();// 创建时间

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}


	public Integer getSubscribestatus() {
		return subscribestatus;
	}

	public void setSubscribestatus(Integer subscribestatus) {
		this.subscribestatus = subscribestatus;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public byte[] getNickname() {
		return nickname;
	}

	public void setNickname(byte[] nickname) {
		this.nickname = nickname;
	}

	public String getNicknameStr() {
		if (this.getNickname() != null) {
			try {
				this.nicknameStr = new String(this.getNickname(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return nicknameStr;
	}

	public void setNicknameStr(String nicknameStr) {
		this.nicknameStr = nicknameStr;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFansStatus() {
		return fansStatus;
	}

	public void setFansStatus(String fansStatus) {
		this.fansStatus = fansStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
}