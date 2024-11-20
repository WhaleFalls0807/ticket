package com.whaleal.modules.sys.service;

import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 15:35
 **/
public interface UserGrabService extends BaseService<UserGrabConfigEntity> {

    /**
     *
     * @param userGrabConfigEntity
     */
    void saveOrUpdate(UserGrabConfigEntity userGrabConfigEntity);

    /**
     *
     * 获取用户的抢单配置
     * @param userId
     * @return
     */
    UserGrabConfigEntity findByUserId(Long userId);

    /**
     * 获取业务员当前count计数
     * @param userId
     * @return
     */
    OrderGrabVO findCountByUserId(Long userId);
}
