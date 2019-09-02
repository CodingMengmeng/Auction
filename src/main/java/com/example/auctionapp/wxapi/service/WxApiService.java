package com.example.auctionapp.wxapi.service;


import com.example.auctionapp.wxapi.domain.*;
import com.example.auctionapp.wxapi.process.*;
import com.example.auctionapp.wxapi.vo.MsgRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;



/**
 * 业务消息处理
 */
@Configuration
public class WxApiService {

	/**
	 * 处理消息 根据用户发送的消息和自己的业务，自行返回合适的消息；
	 * 
	 * @param msgRequest
	 *            接收到的消息
	 * @param mpAccount
	 *            微信公众号
	 */
	public String processMsg(MsgRequest msgRequest, MpAccount mpAccount) {
		String msgtype = msgRequest.getMsgType();// 接收到的消息类型
		String respXml = null;// 返回的内容；
		if (msgtype.equals(MsgType.Text.toString())) {
			/**
			 * 文本消息，一般公众号接收到的都是此类型消息
			 */
			respXml = this.processTextMsg(msgRequest, mpAccount);
		} else if (msgtype.equals(MsgType.Event.toString())) {// 事件消息
			/**
			 * 用户订阅公众账号、点击菜单按钮的时候，会触发事件消息
			 */
			respXml = this.processEventMsg(msgRequest, mpAccount, true);

			// 其他消息类型，开发者自行处理
		} else {
			respXml = "success";
		}
		return respXml;
	}

	// 处理文本消息
	private String processTextMsg(MsgRequest msgRequest, MpAccount mpAccount) {
		String content = msgRequest.getContent();
		if (!StringUtils.isEmpty(content)) {// 文本消息，默认回复订阅消息
			String tmpContent = content.trim();
			MsgText msgText = new MsgText();
			msgText.setContent("您好，欢迎来到七拍商城");
			if (msgText != null) {// 回复文本
				return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, msgText));
			}
		}
		return "success";
	}

	// 处理事件消息
	private String processEventMsg(MsgRequest msgRequest, MpAccount mpAccount, boolean merge) {
		String key = msgRequest.getEventKey();
		if (MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 订阅消息
			processTextMsg(msgRequest ,mpAccount);
		} else if (MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 取消订阅消息

		} else {// 点击事件消息
			if (MsgType.VIEW.toString().equals(msgRequest.getEvent())) {// 点击菜单跳转链接时的事件推送
				return "success";
			}
			if (MsgType.SCANCODE_WAITMSG.toString().equals(msgRequest.getEvent())) {
				return "success";
			}
			if (MsgType.Location.toString().equals(msgRequest.getEvent())) { // 获取地理位置接口
				return "success";
			}
		}
		return "success";
	}
}
