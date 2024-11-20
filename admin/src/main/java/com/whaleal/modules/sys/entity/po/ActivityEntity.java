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
     * 跟进类型
     * 1： 工单操作
     * 2： 电话联系
     * 3： 微信联系
     * 5:
     */
    private Integer activityType;

    /**
     * 10: 创建单子 11：抢单(抢+领) 12：分配单子 13：补充客户信息 14：提交客户信息 15：审核了客户信息 16: 修改单子信息
     *
     *
     *
     */
    private Integer operateType;

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
