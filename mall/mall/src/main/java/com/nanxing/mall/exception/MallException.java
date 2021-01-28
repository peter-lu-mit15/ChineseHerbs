package com.nanxing.mall.exception;

/**
 * @description:  统一异常
 * @author: Mr.Tang
 * @create: 2021/1/15 10:38
 **/
public class MallException  extends RuntimeException{
    private final Integer code;
    private final String message;

    public MallException(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public MallException(MallExceptionEnum exceptionEnum){
        this(exceptionEnum.getCode(),exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
