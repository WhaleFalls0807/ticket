package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.NotificationDTO;
import com.whaleal.modules.sys.entity.po.NotificationEntity;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 13:39
 **/
public interface NotificationService extends BaseService<NotificationEntity> {


    /**
     * 分页查看所有
     * @param params
     * @return
     */
    PageData<NotificationEntity> page(Map<String,Object> params);


    /**
     * 创建消息
     * @param notificationDTO
     */
    void createNotification(NotificationDTO notificationDTO);

    /**
     * 标记单个已读
     * @param id
     */
    void read(Long id);

    /**
     * 删除所有
     */
    void deleteAllRead();

    long queryCount();
}
