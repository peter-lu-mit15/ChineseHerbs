package com.nanxing.mall.util;

import com.nanxing.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: MD5工具
 * @author: Mr.Tang
 * @create: 2021/1/15 11:11
 **/
public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strValue+ Constant.SALT).getBytes()));
    }

    /**
     * description: 测试加密
      * @Param: null
     * @return
     */
    public static void main(String[] args){
        String md5=null;
        try{
            md5=getMD5Str("1234");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        System.out.println(md5);
    }
}
