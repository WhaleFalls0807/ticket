package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.BeanCopyUtil;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.oss.cloud.LocalStorageService;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.dao.OrderDao;
import com.whaleal.modules.sys.entity.dto.*;
import com.whaleal.modules.sys.entity.dto.order.*;
import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.entity.po.BusinessTypeEntity;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.vo.OrderVO;
import com.whaleal.modules.sys.entity.vo.SysUserVO;
import com.whaleal.modules.sys.enums.OrderConstant;
import com.whaleal.modules.sys.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:21
 **/
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private final BusinessTypeService businessTypeService;

    private final ActivityService activityService;

    private final SysUserService sysUserService;

    private final LocalStorageService localStorageService;

    private final CustomerService customerService;


    public OrderServiceImpl(BusinessTypeService businessTypeService,
                            ActivityService activityService,
                            SysUserService sysUserService, LocalStorageService localStorageService, CustomerService customerService, NotificationService notificationService) {
        this.businessTypeService = businessTypeService;
        this.activityService = activityService;
        this.sysUserService = sysUserService;
        this.localStorageService = localStorageService;
        this.customerService = customerService;
        this.notificationService = notificationService;
    }

    @Override
    public PageData<OrderVO> page(Map<String,Object> params) {
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        int deal = Integer.parseInt(params.get("deal").toString());

        wrapper.eq("deal",deal);
        if(!ObjectUtils.isEmpty(params.get("orderStatus"))){
            wrapper.eq("order_status",Integer.parseInt(params.get("orderStatus").toString()));
        }

        if(!ObjectUtils.isEmpty(params.get("reviewType"))){
            // 处理审核类型
            int reviewType = Integer.parseInt(params.get("reviewType").toString());

            if(1 == reviewType){
                wrapper.in("order_status",2,5);
            }else if(2 == reviewType){
                wrapper.in("order_status",3,4,5,6,7);
            }else {
                wrapper.ge("order_status",2)
                        .le("order_status",7);
            }
        }

        if(!ObjectUtils.isEmpty(params.get("ownerId"))){
            wrapper.eq(!ObjectUtils.isEmpty(params.get("ownerId")),"owner_id",Long.parseLong(params.get("ownerId").toString()));
        }

        if(!ObjectUtils.isEmpty(params.get("businessType"))){
           String type = params.get("businessType").toString();
            String[] split = type.split(",");

            List<BusinessTypeEntity> list = businessTypeService.findByType(Arrays.asList(split));

            // 符合条件的order
            List<Long> collect = list.stream().map(BusinessTypeEntity::getOrderId).distinct().collect(Collectors.toList());
            wrapper.in("id",collect);
        }

        if(!ObjectUtils.isEmpty(params.get("keyword"))){
            String keyword = params.get("keyword").toString();
            wrapper.and(wrapper1 -> {
                wrapper1.like("order_name",keyword).or()
                        .like("customer_name",keyword).or()
                        .like("phone",keyword).or()
                        .like("email",keyword);
            });
        }

        if(!ObjectUtils.isEmpty(params.get("startDate"))){
            wrapper.ge(!ObjectUtils.isEmpty(params.get("startDate")),"create_date",params.get("startDate"));
        }
        if(!ObjectUtils.isEmpty(params.get("endDate"))){
            wrapper.ge(!ObjectUtils.isEmpty(params.get("endDate")),"create_date",params.get("endDate"));
        }
        if(ObjectUtils.isEmpty(params.get("sortField"))){
            params.put("sortField","create_date");
        }
        if(ObjectUtils.isEmpty(params.get("isAsc"))){
            params.put("isAsc",false);
        }

        IPage<OrderEntity> orderEntityIPage = baseDao.selectPage(getPage(params, params.get("sortField").toString(), (boolean) params.get("isAsc")),
                wrapper);
        PageData<OrderVO> pageData = getPageData(orderEntityIPage, OrderVO.class);
        for (OrderVO orderVO : pageData.getList()){
            if(deal == 0 || deal == 3){
//                if(){
                    orderVO.setCustomerId(null);
                    orderVO.setCustomerName(null);
                    continue;
//                }
            }

            if(deal == 1 || deal == 2){
                String username = sysUserService.findUsernameByUserId(orderVO.getOwnerId());
                orderVO.setOwnerUsername(username);
            }
            completeInfo(orderVO);
        }
        return pageData;
    }

    private void completeInfo(OrderVO orderVO){
        Integer orderStatus = orderVO.getOrderStatus();
        if(orderStatus == 1){
            // 计算未付款时间

            ActivityEntity lastByAssociationId = activityService.findLastByAssociationId(orderVO.getId(), 11);
            if(!ObjectUtils.isEmpty(lastByAssociationId)){
                long diffInMillis = System.currentTimeMillis() - lastByAssociationId.getCreateDate().getTime();

                long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                orderVO.setDaysUnPay(diffInDays);
            }else {
                orderVO.setDaysUnPay(0L);
            }

        }

        if(orderStatus > 2 && orderStatus < 9){
            ActivityEntity lastByAssociationId = activityService.findLastByAssociationId(orderVO.getId(), 15);
            if(!ObjectUtils.isEmpty(lastByAssociationId)){
                orderVO.setReviewUserId(lastByAssociationId.getCreator());
                orderVO.setReviewUsername(lastByAssociationId.getCreateName());
                orderVO.setReviewDate(lastByAssociationId.getCreateDate());
            }
        }

        if(orderStatus == OrderConstant.SUCCESS){
            ActivityEntity lastByAssociationId = activityService.findLastByAssociationId(orderVO.getId(), 16);
            orderVO.setDealDate(lastByAssociationId.getCreateDate());
        }
    }

    @Override
    public boolean saveSimpleOrder(OrderDTO orderDTO, boolean isInner) {
        OrderEntity orderEntity = new OrderEntity(orderDTO.getOrderName(),orderDTO.getPhone(),orderDTO.getEmail(),orderDTO.getIndustry(),orderDTO.getCustomerName());
        orderEntity.setContent(orderDTO.getRemark());

        if(isInner){
            // 内部人员创建的订单直接默认自己是负责人
            UserDetail user = SecurityUser.getUser();

            orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
            orderEntity.setDeal(1);
            orderEntity.setCreator(user.getId());
            orderEntity.setOwnerId(user.getId());
        }

        boolean insert = insert(orderEntity);
        if(insert){
            String creator ;
            long userId;
            if(isInner){
                creator =  SecurityUser.getUser().getUsername();
                userId = SecurityUser.getUserId();
            }else {
                if(StringUtils.hasText(orderDTO.getCustomerName())){
                    creator = orderDTO.getCustomerName() ;
                }else {
                    creator = orderDTO.getPhone();
                }
                userId = OrderConstant.ANNO_USER_ID;

//                List<Long> collect = sysUserService.listUserByPermission("grab:grab").stream().map(SysUserVO::getId).collect(Collectors.toList());

            }
            activityService.createActivity(new ActivityDTO(orderEntity.getId(),"创建了单子","",1,10,userId,creator));
        }
        return insert;
    }

    private final NotificationService notificationService;

    @Override
    public long distributeOrder(List<Long> orderIds, Long userId) {
        long count = 0;
        for (Long orderId : orderIds) {
            OrderEntity orderEntity = selectById(orderId);

            boolean idPool = orderEntity.getDeal() == 3;
            if (ObjectUtils.isEmpty(orderEntity)) {
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }
            orderEntity.setOwnerId(userId);
            orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
            orderEntity.setDeal(1);
            boolean b = updateById(orderEntity);

            if(b){
                String content;
                int type;
                UserDetail user = SecurityUser.getUser();
                if (Objects.equals(userId, user.getId())) {
                    if(idPool){
                        type = 11;
                        content = user.getUsername() + "抢到了这个单子";
                    }else {
                        type = 19;
                        content = user.getUsername() + "领取了这个单子";
                    }
                } else {
                    content = user.getUsername() + "把单子分配给了" + sysUserService.get(userId).getUsername();
                    type = 12;
                    notificationService.createNotification(new NotificationDTO(orderId,Collections.singletonList(userId),"把单子【" + orderEntity.getOrderName() + "】分配给您",1));
                }
                activityService.createActivity(new ActivityDTO(orderId, content, "", 1, type,user.getId(),user.getUsername()));
                count++;
            }
        }

        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long[] ids) {
        // 删除business type
        businessTypeService.deleteByOrderId(ids);

        // 删除order
        boolean b = deleteBatchIds(Arrays.asList(ids));

        // 删除关联记录
        activityService.deleteByAssId(ids);

        return b;
        // todo 删除文件
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addInformation(OrderEntity orderEntity,OrderUpdateDTO orderUpdateDTO) {
//        if(orderEntity.getOrderStatus() != OrderConstant.DISTRIBUTED && orderEntity.getOrderStatus() != OrderConstant.WAIT_COMMIT_TWICE &&
//            orderEntity.getOrderStatus() != OrderConstant.REVIEW_REJECT && orderEntity.getOrderStatus() != OrderConstant.TWICE_REVIEW_REJECT){
//            // 只有已分配的单子才能补充信息
//            throw new OrderException(OrderExceptionEnum.ONLY_DISTRIBUTE_CAN_OPERATE);
//        }

        BeanUtils.copyProperties(orderUpdateDTO,orderEntity, BeanCopyUtil.getNullPropertyNames(orderUpdateDTO));

        // 1 业务员修改单子信息（首次上传） 2 业务员二次上传 3 admin 回传商标文件
        int type = 0;
        if(!ObjectUtils.isEmpty(orderUpdateDTO.getBusinessTypeList()) && !orderUpdateDTO.getBusinessTypeList().isEmpty()){
            List<BusinessTypeEntity> orderType = orderUpdateDTO.getBusinessTypeList();
            for(BusinessTypeEntity businessTypeEntity : orderType){
                if(ObjectUtils.isEmpty(businessTypeEntity.getId())){
                    businessTypeService.insert(businessTypeEntity);
                }else {
                    businessTypeService.updateById(businessTypeEntity);
                }

                if(!ObjectUtils.isEmpty(businessTypeEntity.getRegiAppAcceptNotice() )|| !ObjectUtils.isEmpty(businessTypeEntity.getRegiAppReceipt()) ||
                    !ObjectUtils.isEmpty(businessTypeEntity.getRegiAppPreApproveNotice()) || !ObjectUtils.isEmpty(businessTypeEntity.getRegiCertificate())){
                    type = 3;
                }else {
                    type = 2;
                }
            }
        }else {
            type = 1;
        }

        String username = SecurityUser.getUser().getUsername();
        if(type == 1){
            activityService.createActivity(new ActivityDTO(orderUpdateDTO.getId(),username + "首次补充了信息","",1,13,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
        }else if(type == 2){
            activityService.createActivity(new ActivityDTO(orderUpdateDTO.getId(),username + "二次补充了信息","",1,13,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
        }else if(type ==3){
            activityService.createActivity(new ActivityDTO(orderUpdateDTO.getId(),username + "补充了商标的回传信息","",1,18,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
            List<Long> longs = new ArrayList<>(Collections.singletonList(orderEntity.getOwnerId()));

            longs.add(orderEntity.getReviewUserId());
            notificationService.createNotification(new NotificationDTO(orderUpdateDTO.getId(),longs, "已上传了【" + orderEntity.getOrderName() + "】的商标回传文件，请及时查收",5));
            // todo 是否要自动完成单子
            orderEntity.setOrderStatus(OrderConstant.FINISH);
        }

        // 信息提交完成处于待提交审核状态
        orderEntity.setUpdater(username);
        updateById(orderEntity);


    }

    @Override
    public void commit(OrderCommitDTO orderCommitDTO) {
        for (Long orderId : orderCommitDTO.getOrderId() ) {
            OrderEntity orderEntity = selectById(orderId);
            if (ObjectUtils.isEmpty(orderEntity)) {
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }
            String username = SecurityUser.getUser().getUsername();
            if(orderEntity.getOrderStatus() == OrderConstant.DISTRIBUTED || orderEntity.getOrderStatus() == OrderConstant.REVIEW_REJECT){
                // 首次提交 / 首次重新提交
                orderEntity.setOrderStatus(OrderConstant.WAIT_REVIEW);
            }else if (orderEntity.getOrderStatus() == OrderConstant.WAIT_COMMIT_TWICE || orderEntity.getOrderStatus() == OrderConstant.TWICE_REVIEW_REJECT) {
                orderEntity.setOrderStatus(OrderConstant.WAIT_REVIEW_TWICE);
            }else {
                throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NOT_SUPPORT);
            }

            updateById(orderEntity);
            String content = username + "提交了单子。";
            if (StringUtils.hasText(orderCommitDTO.getRemark())) {
                content += orderCommitDTO.getRemark();
            }
            activityService.createActivity(new ActivityDTO(orderEntity.getId(), content, "", 1,14, SecurityUser.getUserId(),username));

            // 获取所有具有审核权的人员
            List<SysUserVO> sysUserVOS = sysUserService.listUserByPermission("approve:approve");
            List<Long> collect = sysUserVOS.stream().map(SysUserVO::getId).collect(Collectors.toList());
            notificationService.createNotification(new NotificationDTO(orderId,collect,"提交了单子【" + orderEntity.getOrderName() + "】，请您及时进行审核。",2));
        }
    }

    @Override
    public void review(OrderReviewDTO orderReviewDTO) {
        for(Long orderId : orderReviewDTO.getOrderId()) {
            OrderEntity orderEntity = selectById(orderId);
            if (ObjectUtils.isEmpty(orderEntity)) {
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }

            String username = SecurityUser.getUser().getUsername();
            if (orderReviewDTO.getPass() == 3) {
                // todo 其实没用 需要废弃
                Long[] ids = {orderEntity.getId()};
                delete(ids);
                log.info("{}删除了一个order：{}", username, orderEntity);
            }

            boolean isPass = orderReviewDTO.getPass() == 1;
            if (OrderConstant.WAIT_REVIEW == orderEntity.getOrderStatus()) {
                if (isPass) {
                    orderEntity.setOrderStatus(OrderConstant.WAIT_COMMIT_TWICE);
                } else {
                    orderEntity.setOrderStatus(OrderConstant.REVIEW_REJECT);
                }
            } else if (OrderConstant.WAIT_REVIEW_TWICE == orderEntity.getOrderStatus()) {
                if (isPass) {
                    orderEntity.setOrderStatus(OrderConstant.REVIEW_COMPLETE);
                } else {
                    orderEntity.setOrderStatus(OrderConstant.TWICE_REVIEW_REJECT);
                }
            } else {
                throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NOT_SUPPORT);
            }
            orderEntity.setReviewUserId(SecurityUser.getUserId());
            updateById(orderEntity);

            String content = "审核了单子，审核结果：" + (isPass ? "通过" : "拒绝") + "。";
            if (StringUtils.hasText(orderReviewDTO.getRemark())) {
                content += orderReviewDTO.getRemark();
            }
            activityService.createActivity(new ActivityDTO(orderEntity.getId(), content, "", 1,15,SecurityUser.getUserId(), username));

            notificationService.createNotification(new NotificationDTO(orderId,Collections.singletonList(orderEntity.getOwnerId()),"审核了你的单子【" + orderEntity.getOrderName() + "】，审核结果是：" + (isPass ? "通过。" : "拒绝。") ,3));
        }
    }

    @Override
    public void editStatus(OrderEditDTO orderEditDTO) {
        Long orderId = orderEditDTO.getOrderId();

        OrderEntity orderEntity = selectById(orderId);
        if(ObjectUtils.isEmpty(orderEntity)){
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
        }

        UserDetail user = SecurityUser.getUser();
        if(1 != user.getSuperAdmin() && !Objects.equals(user.getId(), orderEntity.getOwnerId())){
            throw new OrderException(OrderExceptionEnum.NO_PERMISSION_OPERATE);
        }
        LambdaUpdateWrapper<OrderEntity> update = new LambdaUpdateWrapper<>();
        update.eq(OrderEntity::getId,orderId);

        String content = "";
        if(1 == orderEditDTO.getOperateType()){
            update.set(OrderEntity::getDeal,3);
            update.set(OrderEntity::getOrderStatus,OrderConstant.IN_POOL);
            update.set(OrderEntity::getOwnerId,null);
            content = "把单子放回了公海。" + orderEditDTO.getRemark();

        }else if(2 == orderEditDTO.getOperateType()) {
            update.set(OrderEntity::getOwnerId, orderEditDTO.getNewOwnerId());
            content = "把单子转给了" + orderEditDTO.getUsername() + "。" + orderEditDTO.getRemark();
            notificationService.createNotification(new NotificationDTO(orderId,Collections.singletonList(orderEditDTO.getNewOwnerId()),"把单子【" + orderEntity.getOrderName() + "】转给了您",4));

        }else if(3 == orderEditDTO.getOperateType()){
            if(OrderConstant.REVIEW_COMPLETE != orderEntity.getOrderStatus()){
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }

            // 成单
            update.set(OrderEntity::getDeal,2);
            update.set(OrderEntity::getOrderStatus,OrderConstant.SUCCESS);

            content = user.getUsername() + "成交了这个单子。" ;
            if(StringUtils.hasText(content)){
                content = content  + "备注：" + orderEditDTO.getRemark();
            }
            // todo 后续成单也需要修改count计数
            // todo 需要告知给管理员
//           notificationService.createNotification(new NotificationDTO(orderId,Collections.singletonList(orderEditDTO.getNewOwnerId()),"成交了单子",5));
        }
        baseDao.update(update);
        activityService.createActivity(new ActivityDTO(orderEntity.getId(),content,"",1,16,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderEntity electOrder(Long userId) {
        OrderEntity orderEntity = baseDao.electOneOrder();
        if(ObjectUtils.isEmpty(orderEntity)){
            return null;
        }

        // 更新抢到的这个单子的状态
        orderEntity.setDeal(1);
        orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
        orderEntity.setOwnerId(SecurityUser.getUserId());
        updateById(orderEntity);

        activityService.createActivity(new ActivityDTO(orderEntity.getId(),SecurityUser.getUser().getUsername()+"抢到了单子","",1,11,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));

        return orderEntity;
    }


    @Override
    public OrderVO findById(Long id) {
        OrderEntity orderEntity = selectById(id);
        if(ObjectUtils.isEmpty(orderEntity)){
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
        }

        OrderVO orderVO = ConvertUtils.sourceToTarget(orderEntity, OrderVO.class);
        List<BusinessTypeEntity> byOrderId = businessTypeService.findByOrderId(id);

        BigDecimal total = BigDecimal.ZERO;
        for (BusinessTypeEntity businessTypeEntity : byOrderId){
            if(!ObjectUtils.isEmpty(businessTypeEntity.getTotalPrice())){
                total = total.add(businessTypeEntity.getTotalPrice());
            }
        }
        orderVO.setBusinessTypeList(byOrderId);

        CustomerEntity customerEntity = customerService.selectById(orderEntity.getCustomerId());
        if(!ObjectUtils.isEmpty(customerEntity)){
            orderVO.setCustomerWebsite(customerEntity.getWebsite());
        }

        completeInfo(orderVO);
        return orderVO;
    }

    @Override
    public List<Long> findNeedToPool() {
        Calendar instance = Calendar.getInstance();

        instance.setTime(new Date());
        instance.add(Calendar.DAY_OF_MONTH,-10);
        return  baseDao.findUnlinkOrder(instance.getTime());
    }

    @Override
    public void pollUnlinkOrder() {
        Calendar instance = Calendar.getInstance();

        instance.setTime(new Date());
        instance.add(Calendar.DAY_OF_MONTH,-10);
        List<Long> ids = baseDao.findUnlinkOrder(instance.getTime());

        if(ObjectUtils.isEmpty(ids)){
            return;
        }

        LambdaUpdateWrapper<OrderEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

        // todo 需要确定这种情况下哪些信息置空
        lambdaUpdateWrapper.in(OrderEntity::getId,ids);
        lambdaUpdateWrapper.set(OrderEntity::getDeal,3)
                .set(OrderEntity::getOrderStatus,OrderConstant.IN_POOL)
                .set(OrderEntity::getOwnerId,null)
                .set(OrderEntity::getReviewUserId,null);


        baseDao.update(lambdaUpdateWrapper);

        for (Long id : ids){
            activityService.createActivity(new ActivityDTO(id,"工单超时未操作自动转入公海","",1,16,999,"System"));
        }

    }

    @Override
    public void deleteFile(OrderFileDeleteDTO orderFileDeleteDTO) {
        if(StringUtils.hasText(orderFileDeleteDTO.getFieldName())){
            if(1 == orderFileDeleteDTO.getType() ){
                UpdateWrapper<OrderEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",orderFileDeleteDTO.getId());

                TableInfo tableInfo = TableInfoHelper.getTableInfo(OrderEntity.class);
                if (tableInfo == null) {
                    throw new IllegalArgumentException("实体类未被 MyBatis Plus 管理: " + OrderEntity.class.getName());
                }
                for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                    String property = fieldInfo.getProperty();
                    if(StringUtils.pathEquals(orderFileDeleteDTO.getFieldName(),property)){
                        updateWrapper.set(fieldInfo.getColumn(),null);
                    }
                }

                baseDao.update(updateWrapper);
            }else {
                UpdateWrapper<BusinessTypeEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",orderFileDeleteDTO.getId());

                TableInfo tableInfo = TableInfoHelper.getTableInfo(BusinessTypeEntity.class);
                if (tableInfo == null) {
                    throw new IllegalArgumentException("实体类未被 MyBatis Plus 管理: " + OrderEntity.class.getName());
                }
                for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                    if("idcard".equals(orderFileDeleteDTO.getFieldName())){
                        updateWrapper.set("IDCard",null);
                    }
                    String property = fieldInfo.getProperty();
                    if(StringUtils.pathEquals(orderFileDeleteDTO.getFieldName(),property)){
                        updateWrapper.set(fieldInfo.getColumn(),null);
                    }
                }
                businessTypeService.updateByQuery(updateWrapper);
            }
        }
        // 实际删除文件
        localStorageService.deleteFile(orderFileDeleteDTO.getFilePath());
    }

    @Override
    public void issueOrder(OrderIssueDTO orderIssueDTO) {
        Long orderId = orderIssueDTO.getOrderId();
    }

}
