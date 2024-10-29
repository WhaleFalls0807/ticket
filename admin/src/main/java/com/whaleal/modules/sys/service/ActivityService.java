package com.whaleal.modules.sys.service;

import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.ActivityEntity;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:55
 **/
public interface ActivityService extends BaseService<ActivityEntity> {
    List<ActivityEntity> listAllById(Long id);

    void createActivity(ActivityEntity activityEntity);

    void updateActivity(ActivityEntity activityEntity);

}
