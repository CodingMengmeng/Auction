package com.example.auctionapp.wxapi.process;

import com.example.auctionapp.wxapi.vo.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml 消息处理工具类
 * 
 */

public class MsgXmlUtil {
	// 将request 消息 转换成 请求消息对象
	public static MsgRequest parseXml(HttpServletRequest request, String appId,
									  String token, String msgSignature, String timestamp, String nonce) throws Exception {
		MsgRequest msgReq = new MsgRequest();

		// 解析XML
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		parseXml(root, msgReq);
		inputStream.close();
		inputStream = null;
		return msgReq;
	}
	
	private static void parseXml(Element root, MsgRequest msgReq) {
		List<Element> elementList = root.elements();
		// 遍历节点，封装成对象
		for (Element e : elementList) {
			String name = e.getName();
			String text = e.getText();

			if ("MsgType".equals(name)) {
				msgReq.setMsgType(text);
			} else if ("MsgId".equals(name)) {
				msgReq.setMsgId(text);
			} else if ("FromUserName".equals(name)) {
				msgReq.setFromUserName(text);
			} else if ("ToUserName".equals(name)) {
				msgReq.setToUserName(text);
			} else if ("CreateTime".equals(name)) {
				msgReq.setCreateTime(text);
			} else if ("Content".equals(name)) {// 文本消息
				msgReq.setContent(text);
			} else if ("PicUrl".equals(name)) {// 图片消息
				msgReq.setPicUrl(text);
			} else if ("Latitude".equals(name)) {// 地理位置消息
				msgReq.setLatitude(text);
			} else if ("Longitude".equals(name)) {
				msgReq.setLongitude(text);
			} else if ("Scale".equals(name)) {
				msgReq.setScale(text);
			} else if ("Label".equals(name)) {
				msgReq.setLabel(text);
			} else if ("Event".equals(name)) {// 事件消息
				msgReq.setEvent(text);
			} else if ("EventKey".equals(name)) {
				msgReq.setEventKey(text);
			} else if ("ScanCodeInfo".equals(name)) {// 菜单 扫码推事件且弹出“消息接收中”提示框的事件推送
				ScanCodeInfo scanCodeInfo = new ScanCodeInfo();
				scanCodeInfo.setScanResult(text);
				msgReq.setScanCodeInfo(scanCodeInfo);
			}
		}
	}

	public static String textToXml(MsgResponseText text) {
		xstream.alias("xml", text.getClass());
		return xstream.toXML(text);
	}

	public static String imageToXml(MsgResponseImage image) {
		xstream.alias("xml", image.getClass());
		return xstream.toXML(image);
	}

	public static String voiceToXml(MsgResponseVoice voice) {
		xstream.alias("xml", voice.getClass());
		return xstream.toXML(voice);
	}

	public static String videoToXml(MsgResponseVideo video) {
		xstream.alias("xml", video.getClass());
		return xstream.toXML(video);
	}

	public static String musicToXml(MsgResponseMusic music) {
		xstream.alias("xml", music.getClass());
		return xstream.toXML(music);
	}

	public static String newsToXml(MsgResponseNews news) {
		xstream.alias("xml", news.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(news);

	}
	
	public static String toXML(Object obj) {
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}
	

	public static Map<String, String> parseXml(String xml) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;

	}

	/**
	 * 扩展xstream，让xml节点增加CDATA标记
	 */
	public static XStream xstream = new XStream(new XppDriver() {

		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean CDATA = true;

				@Override
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}
				
				@Override
                public String encodeNode(String name) {
                    return name;
                }

                @Override
				protected void writeText(QuickWriter writer, String text) {
					if (CDATA) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	public static void main(String[] args) {
		
	}
	
}
