package com.whaleal.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
     * 单子名称
     *
     */
    private String name;

    private String remark;

    private Integer type;

    /**
     * 状态
     * 0：新建 1：已分配 2：已完成 3：
     */
    private Integer status;


    /**
     *  客户id
     */
    private long customerId;

    /**
     * 负责人id
     */
    private long ownerId;


    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
