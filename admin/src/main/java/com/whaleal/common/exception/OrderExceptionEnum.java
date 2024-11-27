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
    ORDER_NOT_EXISTS(1004,"订单不存在"),

    ORDER_GRAPED_HAS_LIMIT(1005,"此时间周期内已不允许抢单"),


    NO_PERMISSION_UPDATE_CUSTOMER(2003,"没有权限操作非本人的客户"),
    CUSTOMER_EXISTS(2005,"客户信息已存在，无法重复创建"),


    UPLOAD_FILE_ERROR(5001,"上传文件时出现错误"),

    DOWNLOAD_FILE_ERROR(5002,"下载文件时出现错误"),
    FILENAME_IS_EMPTY(5002,"文件名不能为空"),
    FILE_NOT_EXISTS(5004,"文件不存在");


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
