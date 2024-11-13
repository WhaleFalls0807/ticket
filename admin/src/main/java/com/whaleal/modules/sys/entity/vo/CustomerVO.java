package com.whaleal.modules.sys.entity.vo;

import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 15:54
 **/
@Data
public class CustomerVO {

    private Long id;

    private String customerName;

    private String company;

    private String phone;

    private Long ownerUserId;

    private String ownerUserName;

    private Integer dealStatus;

    private String industry;
}
