package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.SysUserDTO;
import com.whaleal.modules.sys.entity.po.OrderEntity;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:20
 **/
public interface OrderService extends BaseService<OrderEntity> {
    /**
     *  分页展示单子信息
     * @param params
     * @return
     */
    PageData<SysUserDTO> page(Map<String, Object> params);
}
