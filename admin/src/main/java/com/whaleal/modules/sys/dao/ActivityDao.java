package com.whaleal.modules.sys.dao;

import com.whaleal.common.dao.BaseDao;
import com.whaleal.modules.sys.entity.po.ActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:54
 **/
@Mapper
public interface ActivityDao extends BaseDao<ActivityEntity> {
    ActivityEntity findLastByAssociationId(@Param("associationId") long id,
                                           @Param("operateType") int type);


    ActivityEntity findLastByUserId(@Param("userId") long userId,
                                    @Param("operateType") int type);

    List<Map<String,Object>> getOrderCountForPart(Date startTime, Date endTime);

    /**
     *
     * @param start
     * @param end
     */
    List<Map<String,Object>> getWechatCount(Date start, Date end);
}
