package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.BusinessTypeDao;
import com.whaleal.modules.sys.entity.po.BusinessTypeEntity;

import com.whaleal.modules.sys.service.BusinessTypeService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 23:51
 **/
@Service
public class BusinessTypeServiceImpl extends BaseServiceImpl<BusinessTypeDao, BusinessTypeEntity> implements BusinessTypeService {


    @Override
    public List<BusinessTypeEntity> findByOrderId(Long id) {
        QueryWrapper<BusinessTypeEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("order_id",id);
        return baseDao.selectList(queryWrapper);
    }

    @Override
    public void deleteByOrderId(Long[] orderIds) {
        QueryWrapper<BusinessTypeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", List.of( orderIds));

        List<BusinessTypeEntity> businessTypeEntities = baseDao.selectList(queryWrapper);

        for (BusinessTypeEntity businessTypeEntity : businessTypeEntities){

        }
        baseDao.delete(queryWrapper);

        // 删除完成后要删除对应的文件

    }

    @Override
    public void updateByQuery(UpdateWrapper<BusinessTypeEntity> updateWrapper) {
        baseDao.update(updateWrapper);
    }
}
