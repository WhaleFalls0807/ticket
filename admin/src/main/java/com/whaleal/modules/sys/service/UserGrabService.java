package com.whaleal.modules.sys.service;

import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;

import java.util.Map;

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
     * 获取所有相关的抢单计数
     * @return
     */
    OrderGrabVO findOrderCount();

    /**
     * 获取业务员当前count计数
     * @param userId
     * @return
     */
    OrderGrabVO findCountByUserId(long userId);

    /**
     * 公共池子抢掉一个单子
     */
    void addGraped(long count);

    /**
     *
     * @param count
     */
    void addRemain(long count);

    /**
     * 减少了一个单子
     *
     * 抢单 / 分配单子
     */
    void grapeOrder(long userId,long count);


    Map<String, Long> initPoolCount();
}
