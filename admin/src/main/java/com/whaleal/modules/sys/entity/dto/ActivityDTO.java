package com.whaleal.modules.sys.entity.dto;

import com.whaleal.modules.sys.enums.OrderConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author lyz
 * @desc
 * @create: 2024-11-05 15:57
 **/
@NoArgsConstructor
@Data
@Schema(title = "跟进")
public class ActivityDTO {

    @Schema(title = "关联业务ID")
    private Long associationId;

    @Schema(title = "跟机内容")
    private String content;

    @Schema(title = "上传的文件路径")
    private String filePath;

    @Schema(title = "跟进类型",description = "1: 工单操作；2: 客户跟进；3: 工单跟进（操作工单时与客户联系）")
    private Integer activityType;

    @Schema(title = "操作类型")
    private Integer operateType;

    @Schema(title = "创建人id")
    private Long creator;


    @Schema(title = "创建人")
    private String createName;

    public ActivityDTO(Long associationId, String content, String filePath, Integer activityType,Integer operateType) {
        this.associationId = associationId;
        this.content = content;
        this.filePath = filePath;
        this.activityType = activityType;
        this.operateType = operateType;
        this.creator = OrderConstant.SYSTEM_ID;
        this.createName = OrderConstant.SYSTEM_NAME;
    }

    public ActivityDTO(Long associationId, String content, String filePath, Integer activityType,Integer operateType, long creator,String createName) {
        this(associationId, content, filePath, activityType, operateType);
        this.creator = creator;
        this.createName = createName;
    }
}
