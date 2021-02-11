package com.nanxing.mall.exception;

/**
 * @description: 异常枚举
 * @author: Mr.Tang
 * @create: 2021/1/15 10:17
 **/
public enum MallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    NEED_TOO_SHORT(10003,"密码长度不能小于6位"),
    NAME_EXISTED(10004,"此账号已注册"),
    INSERT_FAILED(10005,"插入失败，请重试"),
    WONG_PASSWORD(10006,"密码错误"),
    NEED_LOGIN(10007,"用户未登录"),
    UPDATE_FAILED(10008,"更新失败"),
    NEED_ADMIN(10009,"无管理员权限"),
    NAME_NOT_NULL(10010,"参数不能为空"),
    CREATE_FAILED(10011,"新增失败"),
    REQUEST_PARAM_ERROR(10012,"参数错误"),
    DELETE_FAILED(10013,"删除失败"),
    MKDIR_FAILED(10014,"文件夹创建失败"),
    UPLOAD_FAILED(10015,"图片上传失败"),
    NOT_SALE(10016,"商品状态不可售"),
    NOT_ENOUGH(10017,"商品库存不足"),
    CART_ENOUGH(10018,"购物车已勾选的商品为空"),
    NO_ENUM(10019,"为找到对应的枚举类"),
    NO_ORDER(10020,"订单不存在"),
    NOT_YOUR_ORDER(10021,"订单不属于你"),
    WORNG_ORDER_STATUS(10022,"订单状态不符"),
    SEND_EMAIL_ERROR(10023,"发送邮件时发生异常"),
    NOT_USER(100124,"用户不存在"),
    NAMES_EXISTED(100125,"请勿重复添加"),
    FILE_IS_NULL(100126,"文件数据为空"),
    FILE_CLOUN_ISNULL(100127,"文件列必填数据不能为空"),
    SYSTEM_ERROR(20000,"系统异常")
    ;
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    MallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
