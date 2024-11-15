package com.whaleal.modules.sys.entity.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-14 17:22
 **/
@Data
@Schema(title = "分配单子实体")
public class OrderDistributeDTO {

    @Schema(title = "被分配者ID")
    private Long userId;

    @Schema(title = "要分配的order列表")
    private List<Long> orderIds;
}
