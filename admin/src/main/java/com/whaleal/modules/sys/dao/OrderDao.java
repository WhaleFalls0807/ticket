package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:18
 **/
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {
    void findByPage();

    /**
     * 选举一个order出来
     * @return
     */
    OrderEntity electOneOrder();

}
