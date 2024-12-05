package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import com.whaleal.modules.sys.enums.OrderConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 14:37
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("brand_order")
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商标名称
     */
    private String orderName;

    /**
     *  商标内容 = 备注
     */
    private String content;

    /**
     * 行业
     */
    private String industry;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     *  联系人邮件
     */
    private String email;


    /**
     * 上传的合同文件路径
     */
    private String contract;

    /**
     * 支付类型  微信 / 支付宝 / 对公转账
     */
    private String payType;

    /**
     * 支付截图
     */
    private String payment;

    /**
     * 申请方式
     */
    private String applyMethod;

    /**
     * 提交选项
     */
    private String commitOption;

    // 单子级别只承接这部分价格
    /**
     *  总费用
     */
    private BigDecimal totalPrice;

    /**
     * 甲方承担价格
     */
    private BigDecimal aPrice;

    /**
     * 乙方承担价格
     */
    private BigDecimal bPrice;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 单子状态
     */
    private Integer orderStatus;

    /**
     * 0: 新建
     * 1：待成单
     * 2：已成单
     * 3：公海
     */
    private Integer deal;

    /**
     * 审核人ID
     */
    private Long reviewUserId;

    /**
     *  客户id
     */
    private Long customerId;

    /**
     * 负责人id
     */
    private Long ownerId;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    public OrderEntity(String orderName,String phone, String email,String industry,String customerName) {
        this.orderStatus = OrderConstant.CREATED;
        this.deal = 0;
        this.phone = phone;
        this.orderName = orderName;
        this.email = email;
        this.industry = industry;
        this.customerName = customerName;
    }
}
