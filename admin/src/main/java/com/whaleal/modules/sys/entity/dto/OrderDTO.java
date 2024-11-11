package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc 用户通过门户网站提交的单子实体
 * @create: 2024-11-07 11:37
 **/
@Data
@Schema(title = "单子")
public class OrderDTO {

    @Schema(title = "商标名称")
    private String name;

    @Schema(title = "商标简单说明",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;

    @Schema(title = "联系人名称")
    private String username;

    @Schema(title = "联系电话",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String phone;

    @Schema(title = "联系邮箱",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;

    @Schema(title = "行业")
    private String industry;
}
