package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:53
 **/
@Mapper
public interface CustomerDao  extends BaseDao<CustomerEntity> {
}
