package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 16:33
 **/
@Mapper
public interface UserGrabDao extends BaseDao<UserGrabConfigEntity> {
}
