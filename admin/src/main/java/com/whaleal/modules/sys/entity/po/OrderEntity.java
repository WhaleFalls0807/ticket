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
@TableName("order")
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商标名称
     */
    private String name;

    /**
     *  商标内容 可以当作备注使用
     */
    private String content;

    /**
     * 行业
     */
    private String industry;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     *  联系人邮件
     */
    private String email;

    private Long fileId;

    private Long priceId;

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
     * 状态
     * 0：新建 1：已分配 2：已提交审核 3：审核中 4： 审核完成
     */
    private Integer status;

    /**
     * 0: 未成交
     * 1：已成交
     */
    private Integer deal;

    /**
     * 审核人ID
     */
    private Long reviewUserId;

    /**
     * 审核人名称
     */
    @TableField(exist = false)
    private String reviewName;

    /**
     *  客户id
     */
    private Long customerId;

    /**
     *  客户名称
     */
    @TableField(exist = false)
    private String customerName;

    /**
     * 负责人id
     */
    private Long ownerId;

    /**
     *  客户名称
     */
    @TableField(exist = false)
    private String ownerName;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    public OrderEntity(String name,String phone, String email,String industry) {
        this.status = 0;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.industry = industry;
    }
}
