package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.sys.dao.ActivityDao;
import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 21:56
 **/
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityDao, ActivityEntity> implements ActivityService {


    @Override
    public PageData<ActivityEntity> listAllById(Map<String, Object> params) {
        QueryWrapper<ActivityEntity> qw = new QueryWrapper<>();

        IPage<ActivityEntity> data = baseDao.selectPage(getPage(params, "create_time", false),
                qw);

        return getPageData(data,ActivityEntity.class);
    }

    @Override
    public void createActivity(ActivityDTO activityDTO) {
        ActivityEntity entity = ConvertUtils.sourceToTarget(activityDTO, ActivityEntity.class);
        insert(entity);
    }

    @Override
    public void updateActivity(ActivityEntity activityEntity) {

    }
}
