package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lyz
 * @desc 业务文件实体类
 * @create: 2024-11-11 21:31
 **/
@Data
@TableName("order_file")
@AllArgsConstructor
@NoArgsConstructor
public class OrderFileEntity extends BaseEntity {

    /**
     * 所属单子id
     */
    private Long orderId;

    /**
     * 上传的合同文件路径 -- 第一次上传
     */
    private String contract;

//    private String contractName;

    /**
     * 支付类型 这里保存的是上传的支付截图 的地址链接 -- 第一次上传
     */
    private String payType;

//    private String payTypeName;

    /**
     * logo
     */
    private String logo;

//    private String logoName;

    /**
     * 身份证
     */
    private String IDCard;

//    private String IDCardName;

    /**
     * 申请书
     */
    private String applyBook;

//    private String applyBookName;

    /**
     * 委托书
     */
    private String commission;

//    private String commissionName;

    /**
     * 营业执照
     */
    private String businessLicense;

//    private String businessLicenseName;

    /**
     * 盖章合同
     */
    private String sealedContract;

//    private String sealedContractName;

    /**
     * 更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.INSERT)
    private Date updateDate;

    public OrderFileEntity(Long orderId, String contract, String payType) {
        this.orderId = orderId;
        this.contract = contract;
        this.payType = payType;
    }
}
