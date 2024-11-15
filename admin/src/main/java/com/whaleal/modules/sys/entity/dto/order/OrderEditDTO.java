package com.whaleal.modules.sys.entity.dto.order;

import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-15 14:08
 **/
@Data
public class OrderEditDTO {

    private Long orderId;

    /**
     * 1: 放回公海
     * 2： 指派给他人
     */
    private Integer operateType;

    private Long newOwnerId;

    private String username;

    private String remark;
}
