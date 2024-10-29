package com.whaleal.modules.sys.service.impl;

import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.ActivityDao;
import com.whaleal.modules.sys.entity.ActivityEntity;
import com.whaleal.modules.sys.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:56
 **/
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityDao, ActivityEntity> implements ActivityService {

    @Override
    public List<ActivityEntity> listAllById(Long id) {
        return null;
    }

    @Override
    public void createActivity(ActivityEntity activityEntity) {

    }

    @Override
    public void updateActivity(ActivityEntity activityEntity) {

    }
}
