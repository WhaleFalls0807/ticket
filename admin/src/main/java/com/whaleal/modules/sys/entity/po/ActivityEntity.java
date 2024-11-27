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
     * 2： 客户跟进
     * 3： 工单跟进
     * 5:
     */
    private Integer activityType;

    /**
     * 10: 创建单子 11：抢单(抢+领) 12：分配单子 13：补充单子信息 14：提交单子 15：审核了单子 16: 完成了单子
     *
     * 31：微信沟通 32：电话沟通 33：线下沟通
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
