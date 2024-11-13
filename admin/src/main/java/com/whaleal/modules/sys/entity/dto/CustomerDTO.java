package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-04 22:46
 **/
@Schema(title = "保存/修改客户实体")
@Data
public class CustomerDTO {

    @Schema(title = "客户id")
    private Long id;

    @Schema(title = "客户名称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;

    @Schema(title = "公司名称")
    private String company;

    @Schema(title = "公司名称",description = "1： 企业级用户,2： 个人用户")
    private Integer enterprise;

    @Schema(title = "客户来源",requiredMode = Schema.RequiredMode.REQUIRED)
    private String source;

    @Schema(title = "客户网站首页")
    private String website;

    @Schema(title = "客户公司地址")
    private String address;

    @Schema(title = "客户电话",requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(title = "客户联系邮箱")
    private String email;

    @Schema(title = "是否已微信建联")
    private Integer wechat;

    @Schema(title = "客户行业")
    private String industry;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "负责人id")
    private Long ownerUserId;
}
