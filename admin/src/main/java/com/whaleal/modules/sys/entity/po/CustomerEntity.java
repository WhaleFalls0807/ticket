package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 14:38
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("customer")
public class CustomerEntity extends BaseEntity {


    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户来源
     */
    private Integer source;

    /**
     *  是否已跟进  0未跟进1已跟进
     */
    private Integer followup;

    /**
     * 成交状态 0 未成交 1 已成交
     */
    private Integer dealStatus;

    /**
     * 成交时间
     */
    private Date dealTime;

    /**
     * 首要联系人 id
     */
    private Integer contactsId;

    /**
     * 首要联系人ID
     */
    private String mobile;

    /**
     * 手机
     */
    private String telephone;

    /**
     * 行业
     */
    private Integer industry;

    /**
     * 公司首页
     */
    private String website;

    /**
     * 公司联系邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 负责人id
     */
    private Long ownerUserId;

    /**
     * 客户公司地址 - 详细地址
     */
    private String address;

    private String batchId;

    /**
     * 客户状态 1 正常 2锁定 3删除
     */
    private Integer status;

    /**
     * 下次联系时间
     */
    private Date nextTime;

    /**
     * 最后跟进时间
     */
    private Date lastTime;

    /**
     * 最后一次跟进内容 - 此项可能不需要后续
     */
    private String lastContent;

    /**
     * 放入公海时间
     */
    private Date poolTime;

    /**
     * 1 分配 2 领取
     */
    private Integer isReceive;

    /**
     * 接收到客户时间
     */
    private Date receiveTime;

    /**
     * 分配前在公海的负责人的id
     */
    private Long preOwnerUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
