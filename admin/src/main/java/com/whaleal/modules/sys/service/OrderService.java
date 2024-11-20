package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.order.*;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.vo.OrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:20
 **/
public interface OrderService extends BaseService<OrderEntity> {
    /**
     *  分页展示单子信息
     * @param params
     * @return
     */
    PageData<OrderVO> page(Map<String, Object> params);

    /**
     *  普通用户创建普通工单
     * @param orderDTO
     */
    void saveSimpleOrder(OrderDTO orderDTO,boolean isInner);

    /**
     * 管理员创建订单
     * @param orderEntity
     */
    void save(OrderEntity orderEntity);

    /**
     * 管理员简单的分配单子
     * @param orderId
     * @param userId
     * @return
     */
    void distributeOrder(List<Long> orderIds, Long userId);

    /**
     * 批量删除order
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 补充单子信息
     * @param orderUpdateDTO
     */
    void addInformation(OrderEntity orderEntity,OrderUpdateDTO orderUpdateDTO);

    /**
     * 业务员提交单子
     * @param orderCommitDTO
     */
    void commit(OrderCommitDTO orderCommitDTO);

    /**
     * 审核员审核单子
     * @param orderReviewDTO
     */
    void review(OrderReviewDTO orderReviewDTO);

    /**
     * 更改工单状态
     * @param orderEditDTO
     */
    void editStatus(OrderEditDTO orderEditDTO);

    /**
     * 业务员抢单子 - 系统随机分配
     *
     * 当前的算法： 拿第一条新建的
     * @param userId
     */
    OrderEntity electOrder(Long userId);

    /**
     * 获取工单中的计数情况
     * @return
     */
    Map<String, Long> countByType();

    /**
     * 查看order详情
     * @param id
     * @return
     */
    OrderVO findById(Long id);
}
