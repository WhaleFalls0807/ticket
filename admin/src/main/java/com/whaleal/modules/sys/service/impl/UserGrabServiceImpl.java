package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaleal.common.redis.RedisUtils;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.UserGrabDao;
import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;
import com.whaleal.modules.sys.redis.RedisConstant;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.UserGrabService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Long userId = userGrabConfigEntity.getUserId();

        UserGrabConfigEntity byUserId = findByUserId(userId);
        if(ObjectUtils.isEmpty(byUserId)){
            insert(userGrabConfigEntity);
        }else {
            byUserId.setGrain(userGrabConfigEntity.getGrain());
            byUserId.setGap(userGrabConfigEntity.getGap());
            byUserId.setTotalCount(userGrabConfigEntity.getTotalCount());
            byUserId.setGrabGap(userGrabConfigEntity.getGrabGap());

            updateById(byUserId);
        }
    }

    @Override
    public UserGrabConfigEntity findByUserId(Long userId) {
        LambdaQueryWrapper<UserGrabConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,userId);
        return baseDao.selectOne(lambdaQueryWrapper);
    }

    @Override
    public OrderGrabVO findOrderCount() {
        OrderGrabVO orderGrabVO = new OrderGrabVO();
        orderGrabVO.setUserId(SecurityUser.getUserId());

        // 获取单子池子中的统计情况
        Object grapedCount = redisUtils.get(RedisConstant.GRAB_GRAPED_COUNT);
        Object remainCount = redisUtils.get(RedisConstant.GRAB_REMAIN_COUNT);

        Long graped = 0L;
        Long remain = 0L;
        if(!ObjectUtils.isEmpty(grapedCount) && !ObjectUtils.isEmpty(remainCount)){
            graped = Long.parseLong(grapedCount.toString());
            remain = Long.parseLong(remainCount.toString());
        }else {
            Map<String, Long> map = initPoolCount();
            graped = map.get("grapedCount");
            remain = map.get("remainCount");
        }

        orderGrabVO.setGrapedCount(graped);
        orderGrabVO.setRemainCount(remain);
        long totalCount = graped + remain;
        orderGrabVO.setTotalCount(totalCount);
        return orderGrabVO;
    }

    /**
     * 获取用户的抢单数量信息
     * @return
     */
    @Override
    public OrderGrabVO findCountByUserId(long userId) {
        OrderGrabVO orderGrabVO = new OrderGrabVO();

        Object grapedCount = redisUtils.get(RedisConstant.USER_GRAB_COUNT + userId);
        Object totalCount = redisUtils.get(RedisConstant.USER_GRAB_TOTAL_COUNT + userId);

        long graped = 0;
        long total = 0;

        if(!ObjectUtils.isEmpty(grapedCount) && !ObjectUtils.isEmpty(totalCount)){
            graped = Long.parseLong(grapedCount.toString());
            total = Long.parseLong(totalCount.toString());
        }else {
            Map<String, Long> map = initUserCount(userId);
            graped = map.get("graped");
            total = map.get("total");
        }

        long remain = total - graped;
        orderGrabVO.setUserGrapedCount(graped);
        orderGrabVO.setUserTotalCount(total);
        orderGrabVO.setUserRemainCount(remain);

        return orderGrabVO;
    }

    @Override
    public void addGraped(long count) {
        if(count < 1){
            return;
        }
        Object o = redisUtils.get(RedisConstant.GRAB_GRAPED_COUNT);
        // redis 缓存中有数据就更新 没有就不操作
        if(!ObjectUtils.isEmpty(o)){
            long l = Long.parseLong(o.toString()) + count;
            redisUtils.add(RedisConstant.GRAB_GRAPED_COUNT,l);
        }else {
            initPoolCount();
        }
    }

    @Override
    public void addRemain(long count) {
        if(count < 1){
            return;
        }
        Object o = redisUtils.get(RedisConstant.GRAB_REMAIN_COUNT);
        // redis 缓存中有数据就更新 没有就不操作
        if(!ObjectUtils.isEmpty(o)){
            long l = Long.parseLong(o.toString()) + count;
            redisUtils.add(RedisConstant.GRAB_REMAIN_COUNT,l);
        }else {
            initPoolCount();
        }
    }

    @Override
    public void grapeOrder(long userId, long count) {
        // 更新池子中的信息
        addGraped(count);
        addRemain(-count);

        // 更新用户已抢的数量
        Object userCount = redisUtils.get(RedisConstant.USER_GRAB_COUNT + userId);
        if(!ObjectUtils.isEmpty(userCount)){
            long l = Long.parseLong(userCount.toString()) + count;
            if(l < 0){
                return;
            }
            redisUtils.add(RedisConstant.USER_GRAB_COUNT,l);
        }else {
            initUserCount(userId);
        }
    }

    @Override
    public Map<String ,Long> initUserCount(long userId){
        Map<String, Long> map = new HashMap<>();

        //不存在就从数据库查询 并更新redis的缓存
        LambdaQueryWrapper<UserGrabConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,userId);

        UserGrabConfigEntity userGrabConfigEntity = baseDao.selectOne(lambdaQueryWrapper);
        if(ObjectUtils.isEmpty(userGrabConfigEntity)){
            lambdaQueryWrapper.eq(UserGrabConfigEntity::getUserId,null);
            userGrabConfigEntity = baseDao.selectOne(lambdaQueryWrapper);
            if(ObjectUtils.isEmpty(userGrabConfigEntity)){
                // 如果所有配置均没有就采用默认配置
                userGrabConfigEntity = new UserGrabConfigEntity();
                userGrabConfigEntity.setGap(3);
                userGrabConfigEntity.setTotalCount(30L);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.with(LocalTime.MAX);
        LocalDateTime startDate;
        long remain;

        switch (userGrabConfigEntity.getGap()) {
            case 1: // 按月计算剩余时间
                startDate = now.withDayOfMonth(1).with(LocalTime.MIDNIGHT); // 获取当月第一天
                int daysInMonth = YearMonth.from(now).lengthOfMonth(); // 当月总天数
                int remainingDays = daysInMonth - now.getDayOfMonth(); // 本月剩余天数
                remain = ChronoUnit.SECONDS.between(now, endOfDay) + remainingDays * 24 * 3600L;
                break;

            case 2: // 按周计算剩余时间
                int currentDayOfWeek = now.getDayOfWeek().getValue(); // 获取星期几 (1-7)
                startDate = now.minusDays(currentDayOfWeek - 1).with(LocalTime.MIDNIGHT); // 本周开始时间
                int remainingDaysInWeek = 7 - currentDayOfWeek; // 本周剩余天数
                remain = ChronoUnit.SECONDS.between(now, endOfDay) + remainingDaysInWeek * 24 * 3600L;
                break;

            case 4: // 按小时计算剩余时间
                startDate = now.truncatedTo(ChronoUnit.HOURS); // 当前小时的开始时间
                remain = 3600 - (now.getMinute() * 60L + now.getSecond()); // 本小时剩余秒数
                break;

            default: // 按天计算剩余时间
                startDate = now.with(LocalTime.MIDNIGHT); // 当天开始时间
                remain = ChronoUnit.SECONDS.between(now, endOfDay); // 当天剩余秒数
                break;
        }

        long graped = activityService.countBetween(userId,startDate,now);
        long total = userGrabConfigEntity.getTotalCount();

        redisUtils.set(RedisConstant.USER_GRAB_COUNT + userId,graped,remain);
        redisUtils.set(RedisConstant.USER_GRAB_TOTAL_COUNT + userId,total,remain);

        map.put("graped",graped);
        map.put("total",total);
        return map;

    }

    @Override
    public Map<String, Long> initPoolCount(){
        Map<String, Long> map = baseDao.countByType();
        Long remain = map.get("remainCount");
        Long graped = map.get("grapedCount");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.with(LocalTime.MAX);
        long remainSecond = ChronoUnit.SECONDS.between(now, endOfDay); // 当天剩余秒数

        redisUtils.set(RedisConstant.GRAB_GRAPED_COUNT,graped,remainSecond);
        redisUtils.set(RedisConstant.GRAB_REMAIN_COUNT,remain,remainSecond);
        return map;
    }




    @Override
    public Boolean checkGrabInterval(Long userId) {

        // 公海领取单子不占有抢单 次数和时间
        ActivityEntity lastByAssociationId = activityService.findLastByUserId(userId, 11);
        if(ObjectUtils.isEmpty(lastByAssociationId)){
            return true;
        }

        Date createDate = lastByAssociationId.getCreateDate();

        UserGrabConfigEntity byUserId = findByUserId(userId);
        long grab ;
        if(ObjectUtils.isEmpty(byUserId) || ObjectUtils.isEmpty(byUserId.getGrabGap()) || 0 == byUserId.getGrabGap()){
            return true;
        }else {
            grab = byUserId.getGrabGap();
        }
        // 配置的两次抢单间隔 单位为分钟这里转为秒
        long grabGap = grab * 60 * 1000;

        return (System.currentTimeMillis() - createDate.getTime()) >= grabGap;
    }

    public static void main(String[] args) {
        Date date = new Date();

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MINUTE,2);
        long time = instance.getTime().getTime();
        System.out.println(time);

//        System.out.println(date.getTime());
        long l = System.currentTimeMillis();
        System.out.println(l);

        System.out.println(time - l);
        System.out.println(2 * 60 * 1000);
    }
}