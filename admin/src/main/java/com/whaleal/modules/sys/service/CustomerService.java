package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.CustomerEntity;

import java.util.List;

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
     * 获取客户列表
     * @return
     */
    List<CustomerEntity> listALl();

    PageData<CustomerEntity> page();

    void delete(Long[] ids);

}
