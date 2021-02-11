package com.nanxing.mall.common;

import com.google.common.collect.Sets;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @description: 常量
 * @author: Mr.Tang
 * @create: 2021/1/15 11:20
 **/
@Component
public class Constant {
    public static final String SALT="8asf.as[s1.,asf";

    public static final String MALL_USER="mall_user";

    public static String FILE_UPLOAD_DIR;
    public static String EXCEL_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR=fileUploadDir;
    }

    @Value("${file.upload.excel}")
    public void setExcelDir(String fileUploadDir){
        EXCEL_DIR=fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price desc","price asc");
    }

    public interface SaleStatus{
        int NOT_SALE=0;//商品下架状态
        int SALE=1;//商品上架状态
    }

    public interface Cart{
        int UN_CHECK=0;//购物车未选中
        int CHECKED=1;//购物车选中状态
    }
    public enum OrderStatusEnum{
        CANCELED(0,"用户已取消"),
        NOT_PAYED(10,"未付款"),
        PAY(20,"已付款"),
        DELIVERED(30,"已发货"),
        FINISHED(40,"交易完成"),
        CUSTOMER(50,"已提醒用户取药"),
        ;
        private String value;
        private int code;

        OrderStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if(orderStatusEnum.getCode()==code){
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public enum ProductTypeEnum{
        STATUS_P(1,"片状"),
        STATUS_K(2,"块状"),
        STATUS_F(3,"粉末")
        ;
        private String value;
        private int code;

        ProductTypeEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public static ProductTypeEnum codeOf(int code){
            for(ProductTypeEnum productTypeEnum:values()){
                if(productTypeEnum.getCode()==code){
                    return productTypeEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
