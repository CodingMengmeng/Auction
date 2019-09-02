package com.example.auctionapp.wxapi.util;


import com.example.auctionapp.wxapi.vo.TemplateMessage;

import java.util.TreeMap;

public class WXTemplateUtil {

	public static TreeMap<String, TreeMap<String, String>> setCardPayData(String txnDate, String mchntName,
			String shopName, String payAmt, String accBal, String channelName) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("会员卡余额消费成功\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(txnDate, "#000000"));
		params.put("keyword2", TemplateMessage.item(mchntName + " - " + channelName, "#000000"));
		params.put("keyword3", TemplateMessage.item(payAmt + " 元", "#228B22"));
		params.put("keyword4", TemplateMessage.item(accBal + " 元\n\n", "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setHKBPayData(String txnDate, String mchntName,
			String shopName, String payAmt, String accBal, String channelName) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("薪无忧余额消费成功\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(txnDate, "#000000"));
		params.put("keyword2", TemplateMessage.item(mchntName + " - " + channelName, "#000000"));
		params.put("keyword3", TemplateMessage.item(payAmt + " 元", "#228B22"));
		params.put("keyword4", TemplateMessage.item(accBal + " 元\n\n", "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setQuickPayData(String txnDate, String mchntName,
			String shopName, String payAmt) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("微信快捷支付成功\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(txnDate, "#000000"));
		params.put("keyword2", TemplateMessage.item(mchntName + " - " + shopName, "#000000"));
		params.put("keyword3", TemplateMessage.item(payAmt + " 元\n\n", "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setPayBackData(String txnDate, String payAmt) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("微信支付已撤销\n", "#000000"));
		params.put("keynote1", TemplateMessage.item(txnDate, "#000000"));
		params.put("keynote2", TemplateMessage.item(payAmt + " 元\n\n", "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setPurchaseData(String mchntName, String channelName,String desc,
			String txnDate, String payAmt, String accBal) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("尊敬的用户，"+desc, "#000000"));
		params.put("keyword1", TemplateMessage.item(mchntName , "#000000"));
		params.put("keyword2", TemplateMessage.item(payAmt+ " 元", "#228B22"));
		params.put("keyword3", TemplateMessage.item(accBal+ " 元", "#228B22"));
		params.put("keyword4", TemplateMessage.item(channelName, "#000000"));
		params.put("keyword5", TemplateMessage.item(txnDate + "\n\n","#000000"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setRefundData(String transId, String mchntName,
			String shopName, String payAmt, String txnDate, String refundDesc, String channelName) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item(refundDesc + "，款项已原路退回到您的支付账户\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(transId, "#000000"));
		params.put("keyword2", TemplateMessage.item(mchntName + " - " + channelName, "#000000"));
		params.put("keyword3", TemplateMessage.item(payAmt + " 元", "#228B22"));
		params.put("keyword4", TemplateMessage.item(txnDate + "\n\n", "#000000"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}
	
	public static TreeMap<String, TreeMap<String, String>> setResellData(String transId, String payAmt, String txnDate, String bankNo, String resellNum, String productName) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		StringBuilder data = new StringBuilder();
		data.append("订单号[").append(transId).append("]，转让").append(resellNum).append("张").append(productName);
		params.put("first", TemplateMessage.item("您银行卡号" + bankNo + "有一笔卡券转让资金到账，请及时查收\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(payAmt + " 元", "#FF0000"));
		params.put("keyword2", TemplateMessage.item(txnDate, "#000000"));
		params.put("keyword3", TemplateMessage.item(data.toString() + "\n\n", "#000000"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}
		public static TreeMap<String, TreeMap<String, String>> setBalanceDrawData(String transId, String payAmt, String txnDate, String bankNo) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		StringBuilder data = new StringBuilder();
		data.append("提现订单号[").append(transId).append("]");
		params.put("first", TemplateMessage.item("您银行卡号" + bankNo + "有一笔工资提现资金到账，请及时查收\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(payAmt + " 元", "#FF0000"));
		params.put("keyword2", TemplateMessage.item(txnDate, "#000000"));
		params.put("keyword3", TemplateMessage.item(data.toString() + "\n\n", "#000000"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

	public static TreeMap<String, TreeMap<String, String>> setProceedsMsg(String customerPhone, String mchntName, String payAmt, String shopName,String payType, String interfacePrimaryKey, String txnDate) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
		params.put("first", TemplateMessage.item("收到一笔手机尾号"+customerPhone+"的客户付款，请及时查收\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(payAmt+ " 元", "#FF0000"));
		params.put("keyword2", TemplateMessage.item(mchntName + " - " + shopName, "#000000"));
		params.put("keyword3", TemplateMessage.item(payType, "#000000"));
		params.put("keyword4", TemplateMessage.item(interfacePrimaryKey, "#000000"));
		params.put("keyword5", TemplateMessage.item(txnDate+ "\n\n", "#000000"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-52896776", "#FF0000"));
		return params;
	}

}
