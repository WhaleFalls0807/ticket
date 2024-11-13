package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.entity.po.ActivityEntity;

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

    void updateActivity(ActivityEntity activityEntity);

}
