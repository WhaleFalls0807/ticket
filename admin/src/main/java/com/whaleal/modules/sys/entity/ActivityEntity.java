package com.whaleal.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:37
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("activity")
public class ActivityEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 跟进类型
     * 1：工单跟进
     * 2：客户跟进
     */
    private Integer type;

    /**
     * 跟进种类
     * 1：文字跟进
     * 2： 打电话
     * 3： 微信
     *
     */
    private Integer activityType;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 关联业务id
     */
    private Long associationId;

    /**
     * 是否已删除
     */
    private Integer status;
}
