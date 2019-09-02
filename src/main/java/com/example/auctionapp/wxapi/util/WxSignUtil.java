package com.example.auctionapp.wxapi.util;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信验证
 * 
 */

public class WxSignUtil {
	
	
	/**
	 * 根据传入对象进行 参数名ASCII码从小到大排序c
	 * 
	 * @param obj
	 * @return
	 */
	public static SortedMap<String, String> genSortedMap(Object obj) {
		SortedMap<String, String> map = new TreeMap<String, String>();
		if (obj != null) {
			getProperty(map, obj, obj.getClass());// 将当前类属性及其值放入map
		}
		return map;
	}
	
	
	public static void getProperty(SortedMap<String, String> map, Object obj, Class<?> clazz) {
		// 获取f对象对应类中的所有属性域 
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			// 过滤签名字段
			if ("sign".equals(varName) || "serialVersionUID".equals(varName)) {
				continue;
			}
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(obj);
				if (o != null && StringUtils.isNotEmpty(o.toString())) {
					map.put(varName, o.toString());
				}
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @param signature 微信加密签名
	 * @param timestamp tocken
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @return
	 */
	public static boolean validSign(String signature, String tocken, String timestamp, String nonce) {
		String[] paramArr = new String[] { tocken, timestamp, nonce };
		if (paramArr == null || paramArr.length == 0) {
			return false;
		}
		//对token、timestamp、nonce 进行字典排序，并拼接成字符串
		Arrays.sort(paramArr);
		StringBuilder sb = new StringBuilder(paramArr[0]);
		sb.append(paramArr[1]).append(paramArr[2]);
		String ciphertext = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(sb.toString().getBytes());// 对接后的字符串进行sha1加密
			ciphertext = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 将sha1加密后的字符串与  signature 进行比较
		return ciphertext != null ? ciphertext.toUpperCase().equals(signature.toUpperCase()) : false;
	}

	public static String byteToStr(byte[] byteArray) {
		String rst = "";
		for (int i = 0; i < byteArray.length; i++) {
			rst += byteToHex(byteArray[i]);
		}
		return rst;
	}
	
	public static String byteToHex(byte b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b >>> 4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];
		String s = new String(tempArr);
		return s;
	}
	
	/**
	 * MD5编码
	 * 
	 * @param origin
	 *            原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(origin.getBytes("UTF-8"));
			resultString = byteToStr(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}
	
    public static String signature(SortedMap<String,String> items){
    	StringBuilder forSign= new StringBuilder();
    	for(String key:items.keySet()){
    		forSign.append(key).append("=").append(items.get(key)).append("&");
    	}
    	forSign.setLength(forSign.length()-1);
    	String result = encryptSHA1(forSign.toString());
    	return result;
    }
    
    public static String encryptSHA1(String content){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(content.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
	
    public static void main(String[] args) {
//    	String s="{\"appid\":\"wxdc8c2ddbd776853e\",\"attach\":\"2017060712562700000173\",\"bank_type\":\"CFT\",\"cash_fee\":\"45971\",\"coupon_count\":\"1\",\"coupon_fee\":\"29\",\"device_info\":\"WEB\",\"fee_type\":\"CNY\",\"is_subscribe\":\"Y\",\"mch_id\":\"1384887802\",\"nonce_str\":\"aa8df40b9363438eb4415e97995cd2da\",\"openid\":\"o6zC4v9l4AAU5vwU-epgb3IY1AfM\",\"out_trade_no\":\"2017060712562700000173\",\"result_code\":\"SUCCESS\",\"return_code\":\"SUCCESS\",\"sign\":\"6F17EB804C66E54F55005391A2063E11\",\"time_end\":\"20170607125638\",\"total_fee\":\"46000\",\"trade_type\":\"JSAPI\",\"transaction_id\":\"4008122001201706074676643276\"}";
//
//    	JSONObject obj=JSONObject.fromObject(s);
//    	WxBackModel model=(WxBackModel) JSONObject.toBean(obj, WxBackModel.class);
//    	
//		SortedMap<String, String> maps = WxSignUtil.genSortedMap(model);
//		StringBuffer buf=new StringBuffer();
//		 for (Map.Entry<String, String> item : maps.entrySet()){  
//             if (StringUtils.isNotBlank(item.getKey())){  
//                 String key = item.getKey();
//                 String val = item.getValue();
//                 buf.append(key).append("=").append(val);
//                 buf.append("&");  
//             }  
//         }
//		 buf.append("key").append("=").append("");
//		 String signtemp=buf.toString().toLowerCase();
//	
//		 System.out.println(signtemp);
//    	
//		 System.out.println(MD5Encode(signtemp).toUpperCase());
//    	
//    	StringBuffer sb = new StringBuffer();
//		if (obj != null) {
//			sb.append("appid=" + StringUtil.trim(obj.getString("appid")));
//			if (obj.containsKey("attach")) {
//				sb.append("&attach=" + StringUtil.trim(obj.getString("attach")));
//			}
//			sb.append("&bank_type=" + StringUtil.trim(obj.getString("bank_type")));
//			sb.append("&cash_fee=" + StringUtil.trim(obj.getString("cash_fee")));
//			if (obj.containsKey("cash_fee_type")) {
//				sb.append("&cash_fee_type=" + StringUtil.trim(obj.getString("cash_fee_type")));
//			}
//			if (obj.containsKey("coupon_count")) {
//				sb.append("&coupon_count=" + StringUtil.trim(obj.getString("coupon_count")));
//			}
//			if (obj.containsKey("coupon_fee")) {
//				sb.append("&coupon_fee=" + StringUtil.trim(obj.getString("coupon_fee")));
//			}
//			if (obj.containsKey("coupon_fee_$n")) {
//				sb.append("&coupon_fee_$n=" + StringUtil.trim(obj.getString("coupon_fee_$n")));
//			}
//			if (obj.containsKey("coupon_id_$n")) {
//				sb.append("&coupon_id_$n=" + StringUtil.trim(obj.getString("coupon_id_$n")));
//			}
//			if (obj.containsKey("coupon_type_$n")) {
//				sb.append("&coupon_type_$n=" + StringUtil.trim(obj.getString("coupon_type_$n")));
//			}
//			if (obj.containsKey("device_info")) {
//				sb.append("&device_info=" + StringUtil.trim(obj.getString("device_info")));
//			}
//			if (obj.containsKey("err_code")) {
//				sb.append("&err_code=" + StringUtil.trim(obj.getString("err_code")));
//			}
//			if (obj.containsKey("err_code_des")) {
//				sb.append("&err_code_des=" + StringUtil.trim(obj.getString("err_code_des")));
//			}
//			if (obj.containsKey("fee_type")) {
//				sb.append("&fee_type=" + StringUtil.trim(obj.getString("fee_type")));
//			}
//			if (obj.containsKey("is_subscribe")) {
//				sb.append("&is_subscribe=" + StringUtil.trim(obj.getString("is_subscribe")));
//			}
//			sb.append("&mch_id=" + StringUtil.trim(obj.getString("mch_id")));
//			sb.append("&nonce_str=" + StringUtil.trim(obj.getString("nonce_str")));
//			sb.append("&openid=" + StringUtil.trim(obj.getString("openid")));
//			sb.append("&out_trade_no=" + StringUtil.trim(obj.getString("out_trade_no")));
//			sb.append("&result_code=" + StringUtil.trim(obj.getString("result_code")));
//			sb.append("&return_code=" + StringUtil.trim(obj.getString("return_code")));
//			if (obj.containsKey("return_msg")) {
//				sb.append("&return_msg=" + StringUtil.trim(obj.getString("return_msg")));
//			}
//			if (obj.containsKey("settlement_total_fee")) {
//				sb.append("&settlement_total_fee=" + StringUtil.trim(obj.getString("settlement_total_fee")));
//			}
//			sb.append("&time_end=" + StringUtil.trim(obj.getString("time_end")));
//			sb.append("&total_fee=" + StringUtil.trim(obj.getString("total_fee")));
//			sb.append("&trade_type=" + StringUtil.trim(obj.getString("trade_type")));
//			sb.append("&transaction_id=" + StringUtil.trim(obj.getString("transaction_id")));
//			sb.append("&key=68cCSHFD0mAs3H1Dduw0raQMbVRcoDV4");
//		}
//		System.out.println(MD5Encode(sb.toString()).toUpperCase());
	}
}

