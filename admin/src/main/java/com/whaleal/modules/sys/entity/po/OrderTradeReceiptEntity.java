package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 22:04
 **/
@Data
@TableName("order_trade_receipt")
@AllArgsConstructor
@NoArgsConstructor
public class OrderTradeReceiptEntity extends BaseEntity {


    private Long orderId;

    @TableField("business_type_id")
    private Long businessTypeId;

    /**
     * 商标注册申请回执文件
     */
    @TableField("regi_app_receipt")
    private String  regiAppReceipt;

    /**
     * 商标注册申请受理通知书
     */
    @TableField("regi_app_accept_notice")
    private String regiAppAcceptNotice;

    /**
     * 商标注册申请初步审定公告通知书
     */
    @TableField("regi_app_pre_approve_notice")
    private String regiAppPreApproveNotice;

    /**
     * 商标注册证
     */
    @TableField("regi_certificate")
    private String regiCertificate;
}
