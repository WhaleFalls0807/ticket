package com.whaleal.modules.sys.service;

import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.po.OrderFileEntity;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 21:58
 **/
public interface OrderFileService extends BaseService<OrderFileEntity> {

    /**
     *
     * @param id
     * @return
     */
    OrderFileEntity findByOrderId(Long id);

}
