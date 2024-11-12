package com.whaleal.modules.sys.entity.vo;

import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 14:48
 **/
@Data
public class OrderVO {

    private Long id;

    private String name;

    private Long customerId;

    private String customerName;

    private String industry;

    private String phone;

    private Integer orderStatus;

}
