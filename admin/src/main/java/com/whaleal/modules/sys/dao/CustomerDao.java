package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.common.page.PageData;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.vo.CustomerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:53
 **/
@Mapper
public interface CustomerDao  extends BaseDao<CustomerEntity> {


    /**
     * 分页查询客户列表
     * @param params
     * @return
     */
    PageData<CustomerVO> selectPageByParam(@Param("map") Map<String, Object> params,
                                           @Param("offset")long offset,
                                           @Param("limit")long limit);
}
