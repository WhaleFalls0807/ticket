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
     * 更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    //
}
