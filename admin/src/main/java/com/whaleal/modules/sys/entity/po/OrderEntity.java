package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 14:37
 **/
@Data
@EqualsAndHashCode(callSuper=false)
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
     *  业务类型
     *  前端借助数据字典实现 后端暂时以string 接收即可
     */
    private String  businessType;

    /**
     * 申请方式
     */
    private String applyMethod;

    /**
     * 提交选项
     */
    private String commitOption;

    /**
     * 单子状态
     */
    private Integer orderStatus;

    /**
     * 0: 公海
     * 1：待成单
     * 2：已成单
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

    public OrderEntity(String orderName,String phone, String email,String industry,String customerName) {
        this.orderStatus = 0;
        this.deal = 0;
        this.phone = phone;
        this.orderName = orderName;
        this.email = email;
        this.industry = industry;
        this.customerName = customerName;
    }
}
