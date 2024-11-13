package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.OrderFileDao;
import com.whaleal.modules.sys.entity.po.OrderFileEntity;
import com.whaleal.modules.sys.entity.po.OrderPriceEntity;
import com.whaleal.modules.sys.service.OrderFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 23:51
 **/
@Service
public class OrderFileServiceImpl extends BaseServiceImpl<OrderFileDao, OrderFileEntity> implements OrderFileService{


    @Override
    public OrderFileEntity findByOrderId(Long id) {
        QueryWrapper<OrderFileEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("order_id",id);
        return baseDao.selectOne(queryWrapper);
    }

    @Override
    public void deleteByOrderId(Long[] orderIds) {
        QueryWrapper<OrderFileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", List.of( orderIds));
        baseDao.delete(queryWrapper);
    }
}
