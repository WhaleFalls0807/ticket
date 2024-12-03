package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.entity.po.ActivityEntity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:55
 **/
public interface ActivityService extends BaseService<ActivityEntity> {
    /**
     *  获取一个客户/联系人/订单的跟进详情
     * @param params
     * @return
     */
    PageData<ActivityEntity> listAllById(Map<String, Object> params);

    /**
     * 创建一个跟进
     * @param activityDTO
     */
    void createActivity(ActivityDTO activityDTO);

    /**
     * 更新一个跟进
     * @param activityEntity
     */
    void updateActivity(ActivityEntity activityEntity);

    ActivityEntity findLastByAssociationId(long id,int type);


    /**
     * 计算两个时间段之间的跟进数量
     * @param userId
     * @param startDate
     * @param now
     * @return
     */
    long countBetween(Long userId, LocalDateTime startDate, LocalDateTime now);

    /**
     * 主业务删除后 清空所有关联的跟进记录
     * @param ids
     */
    void deleteByAssId(Long[] ids);
}
