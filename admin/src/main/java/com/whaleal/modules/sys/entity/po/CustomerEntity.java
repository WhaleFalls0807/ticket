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
     * 1： 企业级用户
     * 2： 个人用户
     */
    private Integer enterprise;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 客户行业
     */
    private String industry;

    /**
     * 客户来源
     */
    private String source;

    /**
     * 门户网站
     */
    private String website;

    /**
     * 客户地址
     */
    private String address;

    /**
     * 客户电话
     * 暂时作为唯一标识区分客户
     */
    private String phone;

    /**
     * 客户联系邮箱
     */
    private String email;

    /**
     * 是否已建联微信
     * 0: 未建联
     * 1：已建联
     */
    private Integer wechat;

    /**
     * 备注
     */
    private String remark;

    /**
     * 负责人id
     */
    private Long ownerUserId;

    /**
     * 成交状态 0 公海 1 待成交 1 已成交
     */
    private Integer dealStatus;

    /**
     * 成交时间
     */
    private Date dealTime;

    // 先不要逻辑删除
//    /**
//     * 客户状态 1 正常 2锁定 3删除
//     */
//    private Integer status;

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
     * 从公海领取的时间
     */
    private Date receiveTime;

    /**
     * 分配前在公海的负责人的id
     */
    private Long preUserId;

    /**
     * 更新时间
     */
    @TableField(value = "update_date",fill = FieldFill.INSERT)
    private Date updateDate;
}
