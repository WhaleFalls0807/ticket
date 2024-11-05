/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.whaleal.modules.sys.dao;

import com.whaleal.modules.sys.entity.po.DictType;
import com.whaleal.modules.sys.entity.po.SysDictTypeEntity;
import com.whaleal.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典类型
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysDictTypeDao extends BaseDao<SysDictTypeEntity> {

    /**
     * 字典类型列表
     */
    List<DictType> getDictTypeList();

}
