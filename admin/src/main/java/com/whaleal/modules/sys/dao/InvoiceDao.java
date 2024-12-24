package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 16:56
 **/
@Mapper
public interface InvoiceDao extends BaseDao<InvoiceEntity> {
    List<Map<String,Object>> findUserInvoice(Date start, Date end);

}
