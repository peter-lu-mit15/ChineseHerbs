package com.nanxing.mall.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @description: 生成订单NO工具类
 * @author: Mr.Tang
 * @create: 2021/1/16 13:13
 **/
public class OrderCodeFactory {

    /**
     * 订单类别头
     */
    private static  final String ORDER_CODE="1";
    /**
     * 随即编码
     */
    private static final int[] r=new int[]{9,7,6,2,8,5,1,3,0,4};
    /**
     * 用户id和随机数总长度
     */
    private static final int maxLength=5;

    /**
     * 生成固定长度随机码
     * @param n
     * @return
     */
    private static long getRandom(long n) {
        long min=1,max=9;
        for (int i = 1; i < n; i++) {
            min*=10;
            max*=10;
        }
        long rangeLong=(((long)(new Random().nextDouble()*(max-min))))+min;
        return rangeLong;
    }
    /**
     * 生成时间戳
     */
    private static String getDateTime(){
        DateFormat sdf=new SimpleDateFormat("HHmmss");
        return sdf.format(new Date());
    }

    /**
     * 更具id进行加密+随机数组固定长度编码
     * @param id
     * @return
     */
    private static String toCode(Long id) {
        String idStr=id.toString();
        StringBuilder idSB=new StringBuilder();
        for (int i = idStr.length()-1; i>=0 ; i--) {
            idSB.append(r[idStr.charAt(i)-'0']);
        }
        return idSB.append(getRandom(maxLength-idStr.length())).toString();
    }



    /**
     * 生成不带类别标头的编码
     */
    private static synchronized String getCode(Long userId){
        userId=userId==null?10000:userId;
        return getDateTime()+toCode(userId);
    }



    /**
     * 生成订单单号编码
     */
    public static String getOrderCode(Long userId){
        return ORDER_CODE+getCode(userId);
    }

}
