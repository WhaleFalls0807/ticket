package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

        qw.eq("association_id",Long.parseLong(params.get("associationId").toString()));
        if(!ObjectUtils.isEmpty(params.get("activityType"))){
            qw.eq("activity_type",Integer.parseInt(params.get("activityType").toString()));
        }
        IPage<ActivityEntity> data = baseDao.selectPage(getPage(params, "create_date", false),
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
        updateById(activityEntity);
    }

    @Override
    public ActivityEntity findLastByAssociationId(long id,int type) {
        return baseDao.findLastByAssociationId(id,type);
    }

    @Override
    public ActivityEntity findLastByUserId(long id, int type) {
        return baseDao.findLastByUserId(id,type);
    }

    @Override
    public long countBetween(Long userId, LocalDateTime startDate, LocalDateTime now) {
        LambdaQueryWrapper<ActivityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(ActivityEntity::getActivityType,1)
                .eq(ActivityEntity::getOperateType,11)
                .eq(ActivityEntity::getCreator,userId)
                .ge(ActivityEntity::getCreateDate,startDate)
                .le(ActivityEntity::getCreateDate,now);

        return baseDao.selectCount(lambdaQueryWrapper);
    }

    @Override
    public void deleteByAssId(Long[] ids) {
        LambdaQueryWrapper<ActivityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ActivityEntity::getAssociationId, Arrays.asList(ids));

        baseDao.delete(lambdaQueryWrapper);
    }

    @Override
    public List<ActivityEntity> listAllBetween(Date startDate, Date endDate) {
        LambdaQueryWrapper<ActivityEntity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ActivityEntity::getActivityType,1)
                        .eq(ActivityEntity::getOperateType,11)
                        .ge(ActivityEntity::getCreateDate,startDate)
                        .le(ActivityEntity::getCreateDate,endDate);

        return baseDao.selectList(queryWrapper);
    }
}
