package com.whaleal.modules.sys.entity.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc 用户通过门户网站提交的单子实体
 * @create: 2024-11-07 11:37
 **/
@Data
@Schema(title = "customerOrder")
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Schema(title = "商标名称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderName;

    @Schema(title = "商标简单说明",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;

    @Schema(title = "客户名称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;

    @Schema(title = "联系电话",requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(title = "联系邮箱",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;

    @Schema(title = "行业",requiredMode = Schema.RequiredMode.REQUIRED)
    private String industry;
}
