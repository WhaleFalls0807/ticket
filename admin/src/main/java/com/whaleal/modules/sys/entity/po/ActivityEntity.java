package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
     * 关联业务id
     */
    private Long associationId;


    /**
     * 跟进种类
     * 1： 普通跟进
     * 2： 打电话
     * 3： 微信
     * 4: 系统
     */
    private Integer activityType;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 如果上传 上传的文件
     */
    private String filePath;

    /**
     * 创建者用户名
     */
    private String createName;

}
