package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyz
 * @desc 单子的业务类型实体
 * @create: 2024-11-11 21:31
 **/
@Data
@TableName("order_business_type")
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTypeEntity extends BaseEntity {

    /**
     * 所属单子id
     */
    private Long orderId;

    /**
     *  业务类型
     *
     */
    private String  businessType;

    /**
     * 商标名称
     */
    private String brandName;

    /**
     * logo
     */
    private String logo;

    /**
     * 身份证
     */
    @TableField(value = "IDCard")
    private String IDCard;

    /**
     * 申请书
     */
    private String applyBook;


    /**
     * 委托书
     */
    private String commission;


    /**
     * 营业执照
     */
    private String businessLicense;


    /**
     * 盖章合同
     */
    private String sealedContract;

    /**
     * 官费
     */
    private BigDecimal officialPrice;

    /**
     * 代理费
     */
    private BigDecimal agencyPrice;

    /**
     * 总费用
     */
    private BigDecimal totalPrice;

    /**
     * 颁发的商标文件
     */
    private String issueBrand;

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

    /**
     * 更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
