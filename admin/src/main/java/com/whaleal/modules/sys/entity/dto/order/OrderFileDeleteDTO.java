package com.whaleal.modules.sys.entity.dto.order;

import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 16:40
 **/
@Data
public class OrderFileDeleteDTO {

    private Long id;

    private String filePath;

    private String fieldName;

    /**
     * 1: order
     * 2: businessType
     */
    private Integer type;
}
