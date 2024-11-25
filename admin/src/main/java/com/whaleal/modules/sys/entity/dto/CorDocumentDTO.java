package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 17:21
 **/
@Data
@Schema(title = "企业文书实体")
public class CorDocumentDTO {

    private String id;

    @Schema(title = "文书名称")
    private String fileName;

    @Schema(title = "文件路径，由上传接口返回")
    private String filePath;

    @Schema(title = "文书类型")
    private String type;

    @Schema(title = "备注说明")
    private String remark;
}
