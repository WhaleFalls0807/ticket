package com.whaleal.modules.sys.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lyz
 * @desc 用户抢单数据实体 存储在 redis中
 * @create: 2024-11-19 16:55
 **/
@Data
public class OrderGrabVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 关联用户id
     */
    private Long userId;

    /**
     * 已抢单总数
     */
    private Long count;

    /**
     * 总抢单总数
     */
    private Long totalCount;
}
