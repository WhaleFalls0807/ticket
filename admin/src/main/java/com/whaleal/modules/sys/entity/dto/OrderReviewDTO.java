package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 21:27
 **/
@Data
@Schema(title = "order 审核实体")
public class OrderReviewDTO {

    @Schema(title = "order id")
    private Long orderId;

    @Schema(title = "审核结果",description = "1 通过 2 驳回 3 删除")
    private Integer pass;

    @Schema(title = "备注")
    private String remark;

}
