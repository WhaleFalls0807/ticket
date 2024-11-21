package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.BeanCopyUtil;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.dao.OrderDao;
import com.whaleal.modules.sys.entity.dto.*;
import com.whaleal.modules.sys.entity.dto.order.*;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.po.OrderFileEntity;
import com.whaleal.modules.sys.entity.po.OrderPriceEntity;
import com.whaleal.modules.sys.entity.vo.OrderVO;
import com.whaleal.modules.sys.enums.OrderConstant;
import com.whaleal.modules.sys.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:21
 **/
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private final OrderFileService orderFileService;

    private final OrderPriceService orderPriceService;

    private final ActivityService activityService;

    private final SysUserService sysUserService;


    public OrderServiceImpl(OrderFileService orderFileService,
                            OrderPriceService orderPriceService,
                            ActivityService activityService,
                            SysUserService sysUserService) {
        this.orderFileService = orderFileService;
        this.orderPriceService = orderPriceService;
        this.activityService = activityService;
        this.sysUserService = sysUserService;
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
            if(0 == reviewType){
                wrapper.ge("order_status",2)
                        .le("order_status",7);
            }else if(1 == reviewType){
                wrapper.in("order_status",2,5);
            }else if(2 == reviewType){
                wrapper.in("order_status",3,4,5,6,7);
            }
        }

        if(!ObjectUtils.isEmpty(params.get("ownerId"))){
            wrapper.eq(!ObjectUtils.isEmpty(params.get("ownerId")),"owner_id",Long.parseLong(params.get("ownerId").toString()));
        }

        if(!ObjectUtils.isEmpty(params.get("keyword"))){
            String keyword = params.get("keyword").toString();
            wrapper.and(wrapper1 -> {
                wrapper1.like("name",keyword).or()
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
            params.put("isAsc",true);
        }

        IPage<OrderEntity> orderEntityIPage = baseDao.selectPage(getPage(params, params.get("sortField").toString(), (boolean) params.get("isAsc")),
                wrapper);

        if(deal == 0){
            // 公海客户不展示客户信息
            orderEntityIPage.getRecords().forEach(s ->{
                s.setCustomerId(null);
                s.setCustomerName(null);
            });
        }

        return getPageData(orderEntityIPage, OrderVO.class);
    }

    @Override
    public void saveSimpleOrder(OrderDTO orderDTO, boolean isInner) {
        OrderEntity orderEntity = new OrderEntity(orderDTO.getOrderName(),orderDTO.getPhone(),orderDTO.getEmail(),orderDTO.getIndustry(),orderDTO.getCustomerName());

        if(isInner){
            // 内部人员创建的订单直接默认自己是负责人
            UserDetail user = SecurityUser.getUser();

            orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
            orderEntity.setDeal(1);
            orderEntity.setCreator(user.getId());
            orderEntity.setOwnerId(user.getId());

        }
        insert(orderEntity);

        String creator ;
        if(isInner){
            creator =  SecurityUser.getUser().getUsername();
        }else {
            creator = orderDTO.getCustomerName();
        }
        activityService.createActivity(new ActivityDTO(orderEntity.getId(),"创建了单子","",1,10,creator));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(OrderEntity orderEntity) {
        UserDetail user = SecurityUser.getUser();
        Long orderId = orderEntity.getId();

        if(ObjectUtils.isEmpty(orderId)){
            // 新建单子
            orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
            orderEntity.setDeal(1);
            // 创建者默认是负责人
            orderEntity.setOwnerId(user.getId());

        }else {
        }
    }

    @Override
    public void distributeOrder(List<Long> orderIds, Long userId) {
        for (Long orderId : orderIds) {
            OrderEntity orderEntity = selectById(orderId);
            if (ObjectUtils.isEmpty(orderEntity)) {
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }
            orderEntity.setOwnerId(userId);
            orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
            orderEntity.setDeal(1);
            updateById(orderEntity);

            String content;
            int type;
            if (Objects.equals(userId, SecurityUser.getUserId())) {
                content = SecurityUser.getUser().getUsername() + "抢到了这个单子";
                type = 11;
            } else {
                content = SecurityUser.getUser().getUsername() + "把单子分配给了" + sysUserService.get(userId).getUsername();
                type = 12;
            }
            activityService.createActivity(new ActivityDTO(orderId, content, "", 1, type,SecurityUser.getUser().getUsername()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] ids) {
        // 删除order file
        orderFileService.deleteByOrderId(ids);

        // 删除 order price
        orderPriceService.deleteByOrderId(ids);

        // 删除order
        deleteBatchIds(Arrays.asList(ids));

        // todo 删除文件
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addInformation(OrderEntity orderEntity,OrderUpdateDTO orderUpdateDTO) {
        if(orderEntity.getOrderStatus() != OrderConstant.DISTRIBUTED){
            // 只有已分配的单子才能补充信息
            throw new OrderException(OrderExceptionEnum.ONLY_DISTRIBUTE_CAN_OPERATE);
        }

        BeanUtils.copyProperties(orderUpdateDTO,orderEntity, BeanCopyUtil.getNullPropertyNames(orderUpdateDTO));


        // 补充单子信息
        if(StringUtils.hasText(orderUpdateDTO.getContract()) || StringUtils.hasText(orderUpdateDTO.getPayType())){
            // 加载orderFile信息
            // todo 可能会出现重复更新的问题
            OrderFileEntity orderFileEntity = orderFileService.findByOrderId(orderUpdateDTO.getId());
            if(ObjectUtils.isEmpty(orderFileEntity)){
                orderFileEntity = new OrderFileEntity(orderUpdateDTO.getId(), orderUpdateDTO.getContract(), orderUpdateDTO.getPayType());
                orderFileService.insert(orderFileEntity);
            }else {
                if(!ObjectUtils.isEmpty(orderUpdateDTO.getContract()) ){
                    orderFileEntity.setContract(orderUpdateDTO.getContract());
                }
                if(!ObjectUtils.isEmpty(orderUpdateDTO.getPayType())){
                    orderFileEntity.setPayType(orderUpdateDTO.getPayType());
                }
                orderFileService.updateById(orderFileEntity);
            }
        }

        if(StringUtils.hasText(orderUpdateDTO.getLogo()) || StringUtils.hasText(orderUpdateDTO.getIDCard()) ||
            StringUtils.hasText(orderUpdateDTO.getApplyBook()) || StringUtils.hasText(orderUpdateDTO.getCommission()) ||
            StringUtils.hasText(orderUpdateDTO.getBusinessLicense()) || StringUtils.hasText(orderUpdateDTO.getSealedContract())){
            // 加载orderFile信息
            //第二次提交内容 记录一定不为空
            OrderFileEntity orderFile = orderFileService.findByOrderId(orderUpdateDTO.getId());
            if(StringUtils.hasText(orderUpdateDTO.getLogo())){
                orderFile.setLogo(orderUpdateDTO.getLogo());
            }
            if(StringUtils.hasText(orderUpdateDTO.getIDCard())){
                orderFile.setIDCard(orderUpdateDTO.getIDCard());
            }
            if(StringUtils.hasText(orderUpdateDTO.getApplyBook())){
                orderFile.setApplyBook(orderUpdateDTO.getApplyBook());
            }
            if(StringUtils.hasText(orderUpdateDTO.getCommission())){
                orderFile.setCommission(orderUpdateDTO.getCommission());
            }
            if(StringUtils.hasText(orderUpdateDTO.getBusinessLicense())){
                orderFile.setBusinessLicense(orderUpdateDTO.getBusinessLicense());
            }
            if(StringUtils.hasText(orderUpdateDTO.getSealedContract())){
                orderFile.setSealedContract(orderUpdateDTO.getSealedContract());
            }
            orderFileService.updateById(orderFile);
        }

        if(!ObjectUtils.isEmpty(orderUpdateDTO.getTotalPrice()) && orderUpdateDTO.getTotalPrice().compareTo(BigDecimal.ZERO) > 0){
            // 加载order price信息
            // todo 可能会出现重复更新的问题
            OrderPriceEntity orderPriceEntity = orderPriceService.findByOrderId(orderUpdateDTO.getId());
            if(ObjectUtils.isEmpty(orderPriceEntity)){
                orderPriceEntity = new OrderPriceEntity(orderUpdateDTO.getId(), orderUpdateDTO.getOfficialPrice(),orderUpdateDTO.getAgencyPrice(),orderUpdateDTO.getTotalPrice(),orderUpdateDTO.getAPrice(),orderUpdateDTO.getBPrice());
                orderPriceService.insert(orderPriceEntity);
            }else {

                if(!ObjectUtils.isEmpty(orderUpdateDTO.getOfficialPrice()) ){
                    orderPriceEntity.setOfficialPrice(orderUpdateDTO.getOfficialPrice());
                }
                if(!ObjectUtils.isEmpty(orderUpdateDTO.getOfficialPrice()) ){
                    orderPriceEntity.setOfficialPrice(orderUpdateDTO.getOfficialPrice());
                }
                if(!ObjectUtils.isEmpty(orderUpdateDTO.getOfficialPrice()) ){
                    orderPriceEntity.setOfficialPrice(orderUpdateDTO.getOfficialPrice());
                }
                if(!ObjectUtils.isEmpty(orderUpdateDTO.getOfficialPrice()) ){
                    orderPriceEntity.setOfficialPrice(orderUpdateDTO.getOfficialPrice());
                }
                orderPriceService.updateById(orderPriceEntity);
            }
        }
        // 信息提交完成处于待提交审核状态
        orderEntity.setUpdater(SecurityUser.getUser().getUsername());
        updateById(orderEntity);

        String username = SecurityUser.getUser().getUsername();
        activityService.createActivity(new ActivityDTO(orderUpdateDTO.getId(),username + "补充了订单信息","",1,13,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
    }

    @Override
    public void commit(OrderCommitDTO orderCommitDTO) {
        for (Long orderId : orderCommitDTO.getOrderId() ) {
            OrderEntity orderEntity = selectById(orderId);
            if (ObjectUtils.isEmpty(orderEntity)) {
                throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
            }
            String username = SecurityUser.getUser().getUsername();
            if (1 == orderCommitDTO.getCommitType()) {
                // 首次提交
                if (orderEntity.getOrderStatus() == OrderConstant.DISTRIBUTED || orderEntity.getOrderStatus() == OrderConstant.REVIEW_REJECT) {
                    orderEntity.setOrderStatus(OrderConstant.WAIT_REVIEW);
                } else {
                    throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NOT_SUPPORT);
                }
            } else if (2 == orderCommitDTO.getCommitType()) {
                // 二次提交
                if (orderEntity.getOrderStatus() == OrderConstant.WAIT_COMMIT_TWICE || orderEntity.getOrderStatus() == OrderConstant.TWICE_REVIEW_REJECT) {
                    orderEntity.setOrderStatus(OrderConstant.WAIT_REVIEW_TWICE);
                } else {
                    throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NOT_SUPPORT);
                }
            }

            updateById(orderEntity);
            String content = username + "提交了单子。";
            if (StringUtils.hasText(orderCommitDTO.getRemark())) {
                content += orderCommitDTO.getRemark();
            }
            activityService.createActivity(new ActivityDTO(orderEntity.getId(), content, "", 1,14, SecurityUser.getUserId(),username));
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
                    orderEntity.setOrderStatus(OrderConstant.COMPLETE);
                } else {
                    orderEntity.setOrderStatus(OrderConstant.TWICE_REVIEW_REJECT);
                }
            } else {
                throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NOT_SUPPORT);
            }

            String content = "审核了单子，审核结果：" + (isPass ? "通过" : "拒绝") + "。";
            if (StringUtils.hasText(orderReviewDTO.getRemark())) {
                content += orderReviewDTO.getRemark();
            }

            activityService.createActivity(new ActivityDTO(orderEntity.getId(), content, "", 1,15,SecurityUser.getUserId(), username));
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

        String content = "";
        int type;
        if(1 == orderEditDTO.getOperateType()){
            update.set(OrderEntity::getOrderStatus,OrderConstant.CREATED);
            update.set(OrderEntity::getOwnerId,null);
            content = "把单子放回了公海。" + orderEditDTO.getRemark();
        }else {
            update.set(OrderEntity::getOwnerId,orderEditDTO.getNewOwnerId());
            content = "把单子转给了" + orderEditDTO.getUsername() + "。"  + orderEditDTO.getRemark();
        }
        baseDao.update(update);
        activityService.createActivity(new ActivityDTO(orderEntity.getId(),content,"",1,16,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));
    }

    @Override
    public OrderEntity electOrder(Long userId) {
        OrderEntity orderEntity = baseDao.electOneOrder();
        if(ObjectUtils.isEmpty(orderEntity)){
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
        }

        // 检查用户当前是否可以抢单 ，次数是否已经超了限制

        // 更新抢到的这个单子的状态
        orderEntity.setDeal(1);
        orderEntity.setOrderStatus(OrderConstant.DISTRIBUTED);
        orderEntity.setOwnerId(SecurityUser.getUserId());
        updateById(orderEntity);

        activityService.createActivity(new ActivityDTO(orderEntity.getId(),SecurityUser.getUser().getUsername()+"抢到了单子","",1,11,SecurityUser.getUserId(),SecurityUser.getUser().getUsername()));

        return orderEntity;
    }

    @Override
    public Map<String, Long> countByType() {
        return baseDao.countByType();
    }

    @Override
    public OrderVO findById(Long id) {
        OrderEntity orderEntity = selectById(id);
        if(ObjectUtils.isEmpty(orderEntity)){
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXISTS);
        }
        Long ownerId = orderEntity.getOwnerId();
        Integer superAdmin = SecurityUser.getUser().getSuperAdmin();
        if(1 != superAdmin){
            if(!Objects.equals(SecurityUser.getUserId(), ownerId)){
                throw new OrderException(OrderExceptionEnum.NO_PERMISSION_OPERATE);
            }
        }

        OrderVO orderVO = ConvertUtils.sourceToTarget(orderEntity, OrderVO.class);
        orderVO.setOrderPriceVO(orderPriceService.findByOrderId(id));

        orderVO.setOrderFileVO(orderFileService.findByOrderId(id));

        return orderVO;
    }
}
