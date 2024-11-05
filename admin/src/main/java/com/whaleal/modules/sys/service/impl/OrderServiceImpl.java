package com.whaleal.modules.sys.service.impl;

import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.OrderDao;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:21
 **/
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderDao, OrderEntity> implements OrderService {
}
