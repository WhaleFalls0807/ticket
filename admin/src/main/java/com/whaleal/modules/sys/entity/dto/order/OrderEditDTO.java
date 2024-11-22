package com.whaleal.modules.sys.entity.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-15 14:08
 **/
@Data
@Schema(title = "修改工单状态实体")
public class OrderEditDTO {

    @Schema(title = "工单id")
    private Long orderId;

    /**
     * 1: 放回公海
     * 2： 指派给他人
     */
    @Schema(title = "修改工单状态实体")
    private Integer operateType;

    @Schema(title = "修改工单状态实体")
    private Long newOwnerId;

    @Schema(title = "修改工单状态实体")
    private String username;

    @Schema(title = "修改工单状态实体")
    private String remark;
}
