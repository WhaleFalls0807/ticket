package com.whaleal.modules.sys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 17:02
 **/
@Schema(title = "开票保存实体")
@Data
public class InvoiceDTO {

    @Schema(title = "发票id")
    private Long id;

    @Schema(title = "关联用户名")
    private String customerName;

    @Schema(title = "关联用户id")
    private Long customerId;

    @Schema(title = "开票金额")
    private BigDecimal invoicePrice;


    @Schema(title = "开票详情")
    private String invoiceInfo;

    @Schema(title = "开票时间")
    private Date invoiceDate;
}
