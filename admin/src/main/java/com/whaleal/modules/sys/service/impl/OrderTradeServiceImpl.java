package com.whaleal.modules.sys.service.impl;

import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.OrderTradeDAO;
import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.entity.dto.NotificationDTO;
import com.whaleal.modules.sys.entity.dto.order.OrderIssueDTO;
import com.whaleal.modules.sys.entity.po.OrderTradeReceiptEntity;
import com.whaleal.modules.sys.entity.vo.OrderVO;
import com.whaleal.modules.sys.enums.OrderConstant;
import com.whaleal.modules.sys.service.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 22:36
 **/
@Service
public class OrderTradeServiceImpl extends BaseServiceImpl<OrderTradeDAO, OrderTradeReceiptEntity> implements OrderTradeService {

    private final ActivityService activityService;

    private final NotificationService notificationService;

    private final OrderService orderService;


    public OrderTradeServiceImpl(ActivityService activityService, NotificationService notificationService, SysUserService sysUserService, OrderService orderService) {
        this.activityService = activityService;
        this.notificationService = notificationService;
        this.orderService = orderService;
    }

    @Override
    public void issueOrderBrade(OrderIssueDTO orderIssueDTO) {
        OrderVO byId = orderService.findById(orderIssueDTO.getOrderId());

        if(byId.getOrderStatus() != OrderConstant.SUCCESS){
            throw new OrderException(OrderExceptionEnum.UNFINISHED_ORDER_CANT_UPLOAD_TRADE);
        }

        for (OrderTradeReceiptEntity orderTradeReceiptEntity : orderIssueDTO.getTradeList()){
            orderTradeReceiptEntity.setOrderId(orderIssueDTO.getOrderId());
        }
        insertBatch(orderIssueDTO.getTradeList());

        notificationService.createNotification(new NotificationDTO(orderIssueDTO.getOrderId(), Collections.singletonList(byId.getOwnerId() ),"管理员上传了商标回执信息",5));

        activityService.createActivity(new ActivityDTO(orderIssueDTO.getOrderId(),"管理员上传了商标回执信息","",1,18, SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
    }
}
