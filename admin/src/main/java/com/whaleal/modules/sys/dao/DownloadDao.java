package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 18:00
 **/
@Mapper
public interface DownloadDao extends BaseDao<DownloadRecord> {
}
