package com.example.auctionapp.wxapi.process;

/**
 * 消息类型：所有微信涉及到的消息类型统一管理
 */

public enum MsgType {

	Text("text"),//文本消息
	News("news"),//图文消息
	Location("LOCATION"),//地理位置消息
	Image("image"),//图片消息
	Voice("voice"),//语音消息
	Video("video"),//视频消息
	Event("event"),//事件消息
	
	MPNEWS("mpnews"),//群发图文消息
	
	SUBSCRIBE("subscribe"),//订阅消息
	UNSUBSCRIBE("unsubscribe"),//取消订阅
	SCAN("SCAN"),// 扫描带参数二维码事件，用户已关注事件推送
	VIEW("VIEW"),// 点击菜单跳转链接时的事件推送
	SCANCODE_WAITMSG("scancode_waitmsg");// 扫码推事件且弹出“消息接收中”提示框的事件推送
	
	private String name;
	
	private MsgType(String name) {
	     this.name = name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	public String getName() {
		return name;
	}

}
