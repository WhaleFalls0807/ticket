package com.whaleal.modules.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    Map<String,List> findUserGrap(Date startTime, Date endTime);

    /**
     *  获取指定时间范围所有人的开票金额
     * @param start
     * @param end
     * @return
     */
    Map listInvoicePrice(Date start, Date end);

    /**
     * 获取指定时间内微信沟通次数
     * @param start
     * @param end
     * @return
     */
    Map<String,List> getWeChatCount(Date start, Date end);

//    /**
//     * 获取成单率
//     * @param start
//     * @param end
//     * @return
//     */
//    Object getOrderRate(Date start, Date end);
}
