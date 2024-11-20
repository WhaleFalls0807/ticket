package com.whaleal.modules.sys.entity.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-11 23:14
 **/
@Data
@Schema(title = "补充order实体信息")
public class OrderUpdateDTO {

    @Schema(title = "业务员提交的商标订单")
    private Long id;

    @Schema(title = "客户id")
    private Long customerId;

    @Schema(title = "客户名称")
    private String customerName;

    @Schema(title = "名称")
    private String orderName;

    @Schema(title = "商标内容",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String content;

    @Schema(title = "上传的合同文件路径")
    private String contract;

    @Schema(title = "支付类型")
    private String payType;

    @Schema(title = "官费")
    private BigDecimal officialPrice;

    @Schema(title = "代理费")
    private BigDecimal agencyPrice;

    @Schema(title = "总费用")
    private BigDecimal totalPrice;

    @Schema(title = "甲方支付费用")
    private BigDecimal aPrice;

    @Schema(title = "乙方支付费用")
    private BigDecimal bPrice;

    @Schema(title = "行业")
    private String industry;

    @Schema(title = "客户手机号")
    private String phone;

    @Schema(title = "客户邮箱")
    private String email;

    @Schema(title = "业务类型")
    private String  businessType;

    @Schema(title = "申请方式")
    private String applyMethod;

    @Schema(title = "提交选项")
    private String commitOption;

    /**
     * 二次补充内容
     */
    @Schema(title = "logo")
    private String logo;

    @Schema(title = "身份证")
    private String IDCard;

    @Schema(title = "申请书")
    private String applyBook;

    @Schema(title = "委托书")
    private String commission;

    @Schema(title = "营业执照")
    private String businessLicense;

    @Schema(title = "盖章合同")
    private String sealedContract;


}
