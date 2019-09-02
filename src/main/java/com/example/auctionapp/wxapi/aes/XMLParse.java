package com.example.auctionapp.wxapi.aes;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
class XMLParse {

	/**
	 * 提取出xml数据包中的加密消息
	 * @param xmltext 待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException 
	 */
	@SuppressWarnings("unchecked")
	public static Object[] extract(String xmltext) throws AesException     {
		Object[] result = new Object[3];
		try {
			Document document = DocumentHelper.parseText(xmltext);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			
			result[0] = 0;
			for (Element e : elementList) {
				String name = e.getName();
				String text = e.getText();
				if("Encrypt".equals(name)) {
					result[1] = text;
				} else if("ToUserName".equals(name)) {
					result[2] = text;
				}
			}	
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
}
