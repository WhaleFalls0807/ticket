package com.whaleal.modules.sys.service;

import java.util.Date;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-28 16:45
 **/
public interface UserPortraitService {

    /**
     * 获取指定时间范围内所有业务员的抢单数量
     * @param startTime
     * @param endTime
     * @return
     */
    List findUserGrap(Date startTime, Date endTime);
}
