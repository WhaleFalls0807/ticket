package com.whaleal.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 14:38
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("customer")
public class CustomerEntity extends BaseEntity {
}
