package com.whaleal.modules.sys.service.impl;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.OrderDao;
import com.whaleal.modules.sys.entity.dto.OrderDTO;
import com.whaleal.modules.sys.entity.dto.SysUserDTO;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:21
 **/
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Override
    public PageData<SysUserDTO> page(Map<String, Object> params) {
        return null;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity(orderDTO.getName(),orderDTO.getPhone(),orderDTO.getEmail(),orderDTO.getIndustry());

        insert(orderEntity);
    }
}
