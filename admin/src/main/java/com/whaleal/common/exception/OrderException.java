package com.whaleal.common.exception;

import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 22:09
 **/
public class OrderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;

    private int code;

    public OrderException(int code, String msg) {
        super(code + ":" + msg, null, true, true);
        this.code = code;
        this.msg = msg;
    }

    public OrderException(OrderExceptionEnum orderExceptionEnum){
        super(orderExceptionEnum.getMsg());
        this.code = orderExceptionEnum.getCode();
        this.msg = orderExceptionEnum.getMsg();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
