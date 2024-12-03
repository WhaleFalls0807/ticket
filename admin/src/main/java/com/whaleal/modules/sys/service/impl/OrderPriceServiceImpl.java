//package com.whaleal.modules.sys.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.whaleal.common.service.impl.BaseServiceImpl;
//import com.whaleal.modules.sys.dao.OrderPriceDao;
//import com.whaleal.modules.sys.entity.po.OrderPriceEntity;
//import com.whaleal.modules.sys.service.OrderPriceService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author lyz
// * @desc
// * @create: 2024-11-12 23:51
// **/
//@Service
//public class OrderPriceServiceImpl extends BaseServiceImpl<OrderPriceDao, OrderPriceEntity> implements OrderPriceService{
//
//
//    @Override
//    public OrderPriceEntity findByOrderId(Long id) {
//        QueryWrapper<OrderPriceEntity> queryWrapper = new QueryWrapper<>();
//
//        queryWrapper.eq("order_id",id);
//
//        return baseDao.selectOne(queryWrapper);
//    }
//
//    @Override
//    public void deleteByOrderId(Long[] orderIds) {
//        QueryWrapper<OrderPriceEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("order_id", List.of( orderIds));
//        baseDao.delete(queryWrapper);
//    }
//}
