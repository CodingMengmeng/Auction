package com.example.auctionapp.util;

import com.example.auctionapp.core.ProjectConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by zll on 2018/7/19.
 */
public class YeepayUtil {

    private final static Logger logger = LoggerFactory.getLogger(YeepayUtil.class);

    public static boolean verifySign(String text, String masterKey, String signature) {
        boolean isVerified = verify(text, signature, masterKey, "UTF-8");
        if (!isVerified) {
            return false;
        }
        return true;
    }

    public static boolean verify(String text, String sign, String key, String inputCharset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
        return mysign.equals(sign);
    }

    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    /**
     * 验证签名
     * @param maps
     * @return
     */
    public static boolean verifySign (Map<String,Object> maps) {
        String signature = maps.get("signature").toString();
        String transactionId = maps.get("transaction_id").toString();//交易单号
        String transactionType = maps.get("transaction_type").toString();
        String channelType = maps.get("channel_type").toString();
        String transactionFee = maps.get("transaction_fee").toString();
        StringBuffer toSign = new StringBuffer();
        toSign.append(ProjectConstant.YEEPAY_APP_ID).append(transactionId)
                .append(transactionType).append(channelType)
                .append(transactionFee);
        boolean status = verifySign(toSign.toString(), ProjectConstant.YEEPAY_MASTER_SECRET, signature);
        return status;
    }


}
