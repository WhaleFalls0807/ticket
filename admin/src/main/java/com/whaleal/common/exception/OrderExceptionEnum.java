package com.whaleal.common.exception;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 22:12
 **/
public enum OrderExceptionEnum {


    ORDER_STATUS_NOT_SUPPORT(1001,"订单状态不支持此操作"),

    ONLY_DISTRIBUTE_CAN_OPERATE(1002,"只有已分配的订单才支持操作"),
    NO_PERMISSION_OPERATE(1003,"没有权限操作非本人的订单"),
    ORDER_NOT_EXISTS(1004,"没有权限操作非本人的订单"),


    CUSTOMER_EXISTS(2005,"客户信息已存在，无法重复创建");



    OrderExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
