package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 22:02
 **/
@Data
@Schema(title = "提交实体")
public class OrderCommitDTO {

    @Schema(title = "单子id")
    private Long orderId;

    @Schema(title = "提交类型",description = "1: 首次提交 2：二次提交 3：第一次提交被驳回后再次提交 4：第二次提交被驳回后再次提交")
    private Integer commitType;

    @Schema(title = "指定审核人id")
    private Long reviewUserId;

    @Schema(title = "备注说明")
    private String remark;
}
