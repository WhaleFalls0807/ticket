package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 16:54
 **/
@TableName("invoice")
@Data
public class InvoiceEntity extends BaseEntity {

    /**
     * 发票关联客户
     */
    private String customerName;

    /**
     * 关联客户id
     */
    private Long customerId;

    /**
     * 开票金额
     */
    private BigDecimal invoicePrice;

    /**
     * 创建人用户名
     */
    private String creatorName;

    /**
     * 开票详情
     */
    private String invoiceInfo;

    /**
     * 开票时间 这里是为了让业务员也能够填写
     *
     */
    private Date invoiceDate;

}
