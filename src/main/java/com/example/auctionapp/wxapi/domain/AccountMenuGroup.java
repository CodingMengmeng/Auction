package com.example.auctionapp.wxapi.domain;

public class AccountMenuGroup extends BaseEntity {

	private static final long serialVersionUID = -8465480167218192996L;
	private String name;
	private Integer enable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}
