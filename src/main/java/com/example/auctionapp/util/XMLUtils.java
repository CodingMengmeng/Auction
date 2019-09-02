package com.example.auctionapp.util;

import org.jdom.Document;
import org.xml.sax.InputSource;
import org.jdom.input.SAXBuilder;
import org.jdom.Element;
import java.io.StringReader;
import java.util.*;

public class XMLUtils {
    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     * @author ex_yangxiaoyi
     * @see
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    public static Map parseXmlToList(String xml) {
        Map retMap = new HashMap();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0) {
                for (Element element : es) {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }


    public static void map2Element(Map<String, Object> map, StringBuffer sb){
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();){
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value){
                value = "";
            }
            if (value instanceof  List<?>){
                List<Object> list = (List<Object>) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size();i++){
                    Map<String, Object> hm = (Map<String, Object>) list.get(i);
                    map2Element(hm, sb);
                }
                sb.append("</" + key + ">");
            }else {
                if (value instanceof  Map<?,?>){
                    sb.append("<" + key + ">");
                    map2Element((Map<String, Object>) value, sb);
                    sb.append("</" + key + ">");
                }else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }
    }

}
