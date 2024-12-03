package com.whaleal.modules.sys.service.impl;

import com.whaleal.modules.sys.entity.vo.SysUserVO;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.SysUserService;
import com.whaleal.modules.sys.service.UserPortraitService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public List findUserGrap(Date startTime, Date endTime) {
        // 获取所有的业务员信息
        List<SysUserVO> sysUserVOS = sysUserService.listUserByPermission("grab:grab");


        return null;
    }
}
