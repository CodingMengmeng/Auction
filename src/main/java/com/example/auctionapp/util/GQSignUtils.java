package com.example.auctionapp.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class GQSignUtils {
		
    public static String signData(Map<String, Object> params) throws Exception {
        if (params == null || params.size() == 0) {
            return "";
        }
        params = sortMapByKey(params);

        StringBuffer buf = new StringBuffer();
        for (String key : params.keySet()) {
            if (!org.springframework.util.StringUtils.isEmpty(params.get(key))) {
                if (params.get(key) instanceof String && !org.springframework.util.StringUtils.isEmpty(params.get(key))) {
                    buf.append(key).append("=").append((String) params.get(key)).append("&");
                } else if (params.get(key) instanceof String[] && !org.springframework.util.StringUtils.isEmpty(params.get(key))
                        && !org.springframework.util.StringUtils.isEmpty(((String[]) params.get(key))[0]) ) {
                    buf.append(key).append("=").append(((String[]) params.get(key))[0]).append("&");
                }

            }
        }
       
        buf.append("Key").append("=").append("mNIS3lmEi9XyFU6pkkeDZUT4A0bPTaKn");
        String signatureStr = buf.toString();
        String signData = MD5Util.makeMD5String(signatureStr).toUpperCase();
        //System.out.println("请求数据：" + signatureStr + "&Sign=" + signData);
        return signData;
    }

    public static boolean verferSignData(String str) {
        //System.out.println("响应数据：" + str);
        String data[] = str.split("&");
        StringBuffer buf = new StringBuffer();
        String signature = "";
        for (int i = 0; i < data.length; i++) {
            String tmp[] = data[i].split("=", 2);
            if ("Sign".equals(tmp[0])) {
                signature = tmp[1];
            } else {
                buf.append(tmp[0]).append("=").append(tmp[1]).append("&");
            }
        }
        String signatureStr = buf.toString();
        //System.out.println("验签数据：" + signatureStr);
        buf.append("Key").append("=").append("mNIS3lmEi9XyFU6pkkeDZUT4A0bPTaKn");
        signatureStr = buf.toString();
        String signData = MD5Util.makeMD5String(signatureStr).toUpperCase();
        return signature.equals(signData);
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new Comparator<String>(){
                    @Override
                    public int compare(String str1, String str2) {

                        return str1.compareTo(str2);
                    }
                });

        sortMap.putAll(map);

        return sortMap;
    }
}
