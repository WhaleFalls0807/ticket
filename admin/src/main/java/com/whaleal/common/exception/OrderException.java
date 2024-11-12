package com.whaleal.common.exception;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 22:09
 **/
public class OrderException extends RuntimeException {

    private String msg;

    private int code;

    public OrderException(int code, String msg) {
        super(code + ":" + msg, null, true, true);
        this.code = code;
        this.msg = msg;
    }

    public OrderException(OrderExceptionEnum orderExceptionEnum){
        this.code = orderExceptionEnum.getCode();
        this.msg = orderExceptionEnum.getMsg();
    }
}
