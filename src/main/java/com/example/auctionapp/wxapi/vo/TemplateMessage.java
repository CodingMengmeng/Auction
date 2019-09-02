package com.example.auctionapp.wxapi.vo;

import java.util.TreeMap;

/**
 * 发送的模板消息对象
 */
public class TemplateMessage {

	private String touser;// 接收者openid

	private String template_id;// 模板id

	private String url;// 模板跳转链接

	// "miniprogram":{ 未加入
	// "appid":"xiaochengxuappid12345",
	// "pagepath":"index?foo=bar"
	// },

	private TreeMap<String, TreeMap<String, String>> data;// 模板数据

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}

	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}

	/**
	 * 设置模板参数
	 * 
	 * @param value
	 * @param color 可不填
	 * @return
	 */
	public static TreeMap<String, String> item(String value, String color) {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("value", value);
		params.put("color", color);
		return params;
	}
}
