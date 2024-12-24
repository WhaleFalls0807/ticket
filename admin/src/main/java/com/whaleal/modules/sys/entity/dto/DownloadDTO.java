package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-23 11:22
 **/
@Schema(title = "企业文书下载实体")
@Data
public class DownloadDTO {

    @Schema(title = "文件id")
    private Long fileId;

    @Schema(title = "业务员名称",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(title = "客户id",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long customerId;

    @Schema(title = "客户名称",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String customerName;

    @Schema(title = "合同编号",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractNum;

}
