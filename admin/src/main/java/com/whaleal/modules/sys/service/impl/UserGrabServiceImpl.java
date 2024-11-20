package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaleal.common.redis.RedisUtils;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.UserGrabDao;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;
import com.whaleal.modules.sys.redis.RedisConstant;
import com.whaleal.modules.sys.redis.SysParamsRedis;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.UserGrabService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 16:32
 **/
@Service
public class UserGrabServiceImpl extends BaseServiceImpl<UserGrabDao,UserGrabConfigEntity> implements UserGrabService {

    private final RedisUtils redisUtils;

    private final ActivityService activityService;

    public UserGrabServiceImpl(RedisUtils redisUtils, ActivityService activityService) {
        this.redisUtils = redisUtils;
        this.activityService = activityService;
    }


    @Override
    public void saveOrUpdate(UserGrabConfigEntity userGrabConfigEntity) {
        if(ObjectUtils.isEmpty(userGrabConfigEntity.getId())){
            insert(userGrabConfigEntity);
        }else {
            updateById(userGrabConfigEntity);
        }
    }

    @Override
    public UserGrabConfigEntity findByUserId(Long userId) {
        LambdaQueryWrapper<UserGrabConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,userId);
        return baseDao.selectOne(lambdaQueryWrapper);
    }

    @Override
    public OrderGrabVO findCountByUserId(Long userId) {
        OrderGrabVO orderGrabVO = new OrderGrabVO();
        orderGrabVO.setUserId(userId);

        Object count = redisUtils.get(RedisConstant.USER_GRAB_COUNT + userId);
        Object totalCount = redisUtils.get(RedisConstant.USER_GRAB_TOTAL_COUNT + userId);

        if(!ObjectUtils.isEmpty(count) && !ObjectUtils.isEmpty(totalCount)){
            orderGrabVO.setCount(Long.parseLong(count.toString()));
            orderGrabVO.setTotalCount(Long.parseLong(totalCount.toString()));
        }else {
            //不存在就从数据库查询 并更新redis的缓存
            LambdaQueryWrapper<UserGrabConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,userId);

            UserGrabConfigEntity userGrabConfigEntity = baseDao.selectOne(lambdaQueryWrapper);
            if(ObjectUtils.isEmpty(userGrabConfigEntity)){
                lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,null);
                userGrabConfigEntity = baseDao.selectOne(lambdaQueryWrapper);
            }

            getGrabCount(orderGrabVO,userGrabConfigEntity);
            redisUtils.set(RedisConstant.USER_GRAB_COUNT + userId,orderGrabVO.getCount().toString());
            redisUtils.set(RedisConstant.USER_GRAB_COUNT + userId,orderGrabVO.getTotalCount().toString());
        }
        return orderGrabVO;
    }


    private OrderGrabVO getGrabCount(OrderGrabVO orderGrabVO,UserGrabConfigEntity userGrabConfigEntity){
        if(ObjectUtils.isEmpty(userGrabConfigEntity)){
            userGrabConfigEntity = new UserGrabConfigEntity();
            userGrabConfigEntity.setGap(3);
            orderGrabVO.setTotalCount(30L);
        }else {
            orderGrabVO.setTotalCount(userGrabConfigEntity.getTotalCount());
        }

        Integer interval = userGrabConfigEntity.getGap();
        LocalDateTime startDate ;
        LocalDateTime now = LocalDateTime.now();
        switch (interval){
            case 1:
                startDate = now.minusMonths(1);
                break;
            case 2:
                startDate = now.minusDays(7);
                break;
            case 4:
                startDate = now.minusHours(1);
                break;
            default:
                startDate = now.minusDays(1);
                break;
        }

        long count = activityService.countBetween(orderGrabVO.getUserId(),startDate,now);
        orderGrabVO.setCount(count);
        return orderGrabVO;
    }
}
