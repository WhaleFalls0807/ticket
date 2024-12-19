package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.dto.order.OrderUpdateDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.vo.CustomerVO;

import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:51
 **/
public interface CustomerService extends BaseService<CustomerEntity> {
    /**
     * 新增或更新实体
     * @param customerDTO
     */
    void saveOrUpdate(CustomerDTO customerDTO);

    /**
     * 管理员获取客户列表
     * @return
     */
    List<CustomerEntity> listALl();

    /**
     *  根据查询条件获取客户分页列表
     * @param params
     * @return
     */
    PageData<CustomerVO> page(Map<String, Object> params);

    /**
     * 删除客户
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     *  检查用户名是否可用
     * @param customerName
     * @return
     */
    boolean checkCustomer(String customerName);

    /**
     * 根据工单信息加载客户信息（创建）
     * @param orderEntity
     */
    CustomerEntity loadCustomer(OrderUpdateDTO orderEntity);

    void deleteCustomer(Long[] ids);

}
