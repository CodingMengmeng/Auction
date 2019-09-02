package com.example.auctionapp.util;

import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class Md5SaltTool {

    private static final String HEX_NUMS_STR = "0123456789ABCDEF";
    private static final Integer SALT_LENGTH = 12;

    public static String getMD5(String source) {
        String s = null;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};// 用来将字节转换成16进制表示的字符
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source.getBytes());
            byte[] tmp = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char[] str = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            // 进制需要 32 个字符
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
                // 进制字符的转换
                byte byte0 = tmp[i];// 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换
            }
            s = new String(str);// 换后的结果转换为字符串
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    /**
     *  密码加密后 转换成大写
     * @param sourcePwd
     * @return
     */
    public static String getCustomerPwdMd5(String sourcePwd) {
        sourcePwd=sourcePwd.toUpperCase();
        String pwd =getMD5(sourcePwd);
        return pwd.toUpperCase();
    }

    /**
     *  前端加密三次 md5(md5(md5(密码))+salt) 验证方法
     * @param sourcePwd 前端提交的密码
     * @param sourceSalt
     * @param targetPwd 数据库存储的密码
     * @return
     */
    public static boolean verifyPwd(String sourcePwd , String sourceSalt , String targetPwd){
        //如果  sourcePwd 或 targetPwd 为null 则返回验证失败
        if(StringUtils.isEmpty(sourcePwd) || StringUtils.isEmpty(targetPwd)){
            return false;
        }
        String md5Pwd;
        if(StringUtils.isNotEmpty(sourceSalt)){
            md5Pwd=getMD5(targetPwd+sourceSalt).toUpperCase();
        }else{
            md5Pwd=getMD5(targetPwd).toUpperCase();
        }
        //返回比较值
        return md5Pwd.equals(sourcePwd.toUpperCase());
    }


    /**
     *  前端密码加密一次验证方式
     * @param sourcePwd 前端提交的密码
     * @param targetPwd 数据库存储的密码
     * @return
     */
    public static boolean verifyPwd(String sourcePwd , String targetPwd){
        //如果  sourcePwd 或 targetPwd 为null 则返回验证失败
        if(StringUtils.isEmpty(sourcePwd) || StringUtils.isEmpty(targetPwd)){
            return false;
        }
        String md5Pwd=getMD5(sourcePwd.toUpperCase()).toUpperCase();
        //返回比较值
        return md5Pwd.equals(targetPwd.toUpperCase());
    }

}