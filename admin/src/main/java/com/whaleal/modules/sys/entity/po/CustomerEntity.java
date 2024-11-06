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
    private String source;

    /**
     * 客户公司首页
     */
    private String website;

    /**
     * 客户公司地址
     */
    private String address;

    /**
     *  联系人
     */
    private String contactName;

    /**
     * 客户电话
     */
    private String phone;

    /**
     * 客户联系邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 负责人id
     */
    private Long ownerUserId;

    /**
     * 成交状态 0 未成交 1 已成交
     */
    private Integer dealStatus;

    /**
     * 成交时间
     */
    private Date dealTime;


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
     * 1 已分配 -> 正常客户
     * 2 未分配 -> 在公海
     */
    private Integer assigned;

    /**
     * 从公海领取的时间
     */
    private Date receiveTime;

    /**
     * 分配前在公海的负责人的id
     */
    private Long preUserId;
}
