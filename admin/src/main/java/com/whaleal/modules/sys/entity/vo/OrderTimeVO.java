package com.whaleal.modules.sys.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-27 13:25
 **/
@Data
public class OrderTimeVO {

    private Date createTime;

    private Date dealTime;

    private Date reviewTime;
}
