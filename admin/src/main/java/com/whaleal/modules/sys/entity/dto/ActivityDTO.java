package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


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

    @Schema(title = "操作类型")
    private Integer operateType;

    @Schema(title = "创建人id",description = "外部人员 为888")
    private Long creator;

    @Schema(title = "创建人")
    private String createName;

    public ActivityDTO(Long associationId, String content, String filePath, Integer activityType,String createName) {
        this.associationId = associationId;
        this.content = content;
        this.filePath = filePath;
        this.activityType = activityType;
        this.creator = 999L;
        this.createName = createName;
    }

    public ActivityDTO(Long associationId, String content, String filePath, Integer activityType, long creator,String createName) {
        this.associationId = associationId;
        this.content = content;
        this.filePath = filePath;
        this.activityType = activityType;
        this.creator = creator;
        this.createName = createName;
    }
}
