/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.whaleal.modules.log.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.log.entity.SysLogOperationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface SysLogOperationDao extends BaseDao<SysLogOperationEntity> {
	
}
