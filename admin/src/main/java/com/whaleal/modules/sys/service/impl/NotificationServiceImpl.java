package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.dao.BaseDao;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.job.task.ConvertPoolTask;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.dao.NotificationDao;
import com.whaleal.modules.sys.entity.dto.NotificationDTO;
import com.whaleal.modules.sys.entity.po.NotificationEntity;
import com.whaleal.modules.sys.service.NotificationService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 13:41
 **/
@Service
public class NotificationServiceImpl extends BaseServiceImpl<NotificationDao, NotificationEntity> implements NotificationService {
    @Override
    public PageData<NotificationEntity> page(Map<String, Object> params) {

        LambdaQueryWrapper<NotificationEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(NotificationEntity::getId, SecurityUser.getUserId());
        if(!ObjectUtils.isEmpty(params.get("read"))){
            int read = Integer.parseInt(params.get("read").toString());
            if(2 != read){
                lambdaQueryWrapper.eq(NotificationEntity::getRead,read);
            }
        }

        IPage<NotificationEntity> create_date = baseDao.selectPage(getPage(params, "create_date", false),
                lambdaQueryWrapper);

        return getPageData(create_date,NotificationEntity.class);
    }

    @Override
    public void createNotification(NotificationDTO notificationDTO){
        List<NotificationEntity> list = new ArrayList<>();
        for (Long receive : notificationDTO.getReceiveIds()){
            NotificationEntity notificationEntity = ConvertUtils.sourceToTarget(notificationDTO, NotificationEntity.class);
            notificationEntity.setRead(0);
            notificationEntity.setReceiveId(receive);
            UserDetail user = SecurityUser.getUser();

            Long userId ;
            String username;
            if(ObjectUtils.isEmpty(user)){
                userId = user.getId();
                username = user.getUsername();
            }else {
                userId = 999L;
                username = "System";
            }
            notificationEntity.setCreator(userId);
            notificationEntity.setCreateName(username);

            list.add(notificationEntity);
        }

        insertBatch(list);
    }

    @Override
    public void read(Long id) {
        LambdaUpdateWrapper<NotificationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(NotificationEntity::getRead,0)
                .eq(NotificationEntity::getReceiveId,SecurityUser.getUserId());

        if(!ObjectUtils.isEmpty(id)){
            lambdaUpdateWrapper.eq(NotificationEntity::getId, id);
        }

        lambdaUpdateWrapper.set(NotificationEntity::getRead,1);
        baseDao.update(lambdaUpdateWrapper);
    }

    @Override
    public void deleteAllRead() {
        Long userId = SecurityUser.getUserId();

        QueryWrapper<NotificationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("receive_id",userId)
                .eq("read",1);
        baseDao.delete(wrapper);
    }
}
