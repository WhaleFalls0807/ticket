package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author lyz
 * @desc
 * @create: 2024-11-05 15:57
 **/
@Data
@Schema(title = "跟进")
public class ActivityDTO {

    @Schema(title = "关联业务ID")
    private Long associationId;

    @Schema(title = "跟机内容")
    private String content;

    @Schema(title = "上传的文件路径")
    private String filePath;

    @Schema(title = "跟进类型")
    private Integer activityType;

}
