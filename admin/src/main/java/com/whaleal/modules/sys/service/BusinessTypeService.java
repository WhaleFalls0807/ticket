package com.whaleal.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.po.BusinessTypeEntity;


import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 21:58
 **/
public interface BusinessTypeService extends BaseService<BusinessTypeEntity> {

    /**
     *
     * @param id
     * @return
     */
    List<BusinessTypeEntity> findByOrderId(Long id);

    void deleteByOrderId(Long[] orderIds);

    void updateByQuery(UpdateWrapper<BusinessTypeEntity> updateWrapper);

    /**
     * 根据具体类型查找记录
     * @param type
     * @return
     */
    List<BusinessTypeEntity> findByType(List<String> type);

}
