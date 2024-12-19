package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 11:17
 **/
@TableName("notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends BaseEntity {

    /**
     *
     */
    private Long associationId;

    private Long receiveId;

    private String content;

    private String createName;

    private Integer type;

    /**
     * 是否已读
     * 0：未读
     * 1：已读
     *
     */
    private Integer isRead;

}
