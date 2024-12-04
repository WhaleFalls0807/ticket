package com.whaleal.modules.sys.service.impl;

import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.entity.vo.SysUserVO;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.SysUserService;
import com.whaleal.modules.sys.service.UserPortraitService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-21 10:05
 **/
@Service
public class UserPortraitServiceImpl implements UserPortraitService {

    private final SysUserService sysUserService;

    private final ActivityService activityService;

    public UserPortraitServiceImpl(SysUserService sysUserService, ActivityService activityService) {
        this.sysUserService = sysUserService;
        this.activityService = activityService;
    }

    @Override
    public Map<String,List> findUserGrap(Date startTime, Date endTime) {
        Map<String,List> result = new HashMap<>();

        List<String> username = new ArrayList<>();
        List<Integer> count = new ArrayList<>();

        List<ActivityEntity> activityEntities = activityService.listAllBetween(startTime, endTime);

        Map<String, List<ActivityEntity>> collect = activityEntities.stream().collect(Collectors.groupingBy(ActivityEntity::getCreateName));
        collect.entrySet().forEach(s -> {
            username.add(s.getKey());
            count.add(s.getValue().size());
        });

        result.put("username",username);
        result.put("count",count);
        return result;
    }
}
