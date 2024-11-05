/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
	

}
