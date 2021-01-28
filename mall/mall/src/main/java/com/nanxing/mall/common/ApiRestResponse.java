package com.nanxing.mall.common;

import com.nanxing.mall.exception.MallExceptionEnum;

/**
 * @description: 通用返回对象
 * @author: Mr.Tang
 * @create: 2021/1/15 10:07
 **/
public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    private T data;

    private static final int OK_CODE=200;
    private static final String OK_MSG="SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ApiRestResponse() {
        this(OK_CODE,OK_MSG);
    }

    /**
     * description: 无数据成功返回
      * @Param: null
     * @return
     */
    public static <T> ApiRestResponse<T> success(){
        return new ApiRestResponse<>();
    }

    /**
     * description: 携带数据成功返回
      * @Param: null
     * @return
     */
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse response=new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    /**
     * description: 用户自定义错误信息返回
     * @return
     */
    public static <T> ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<>(code,msg);
    }

    /**
     * description: 通过枚举系统定义错误信息
     * @return
     */
    public static <T> ApiRestResponse<T> error(MallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(),ex.getMsg());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }
}
