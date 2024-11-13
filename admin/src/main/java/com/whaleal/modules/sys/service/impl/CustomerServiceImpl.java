package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.dao.CustomerDao;
import com.whaleal.modules.sys.entity.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.dto.OrderUpdateDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.vo.CustomerVO;
import com.whaleal.modules.sys.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:52
 **/
@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDao, CustomerEntity> implements CustomerService {

    @Override
    public void saveOrUpdate(CustomerDTO customerDTO) {
        CustomerEntity entity = ConvertUtils.sourceToTarget(customerDTO, CustomerEntity.class);

        if(ObjectUtils.isEmpty(customerDTO.getId())){
            if(!checkCustomer(customerDTO.getPhone())){
                throw new OrderException(OrderExceptionEnum.CUSTOMER_EXISTS);
            }
            // 新增
            if(ObjectUtils.isEmpty(entity.getOwnerUserId())){
                entity.setOwnerUserId(SecurityUser.getUserId());
            }
            if(ObjectUtils.isEmpty(customerDTO.getOwnerUserId())){
                entity.setDealStatus(0);
            }else {
                entity.setDealStatus(1);
            }
            insert(entity);
            log.info("新增一条客户信息");
        }else {
            Long id = customerDTO.getId();
            CustomerEntity customerEntity = selectById(id);
            UserDetail user = SecurityUser.getUser();
            if(user.getSuperAdmin() != 1){
                if(!Objects.equals(user.getId(), customerEntity.getOwnerUserId())){
                    throw new OrderException(OrderExceptionEnum.NO_PERMISSION_UPDATE_CUSTOMER);
                }
            }
            // 更新
            updateById(entity);
            log.info("修改了一条客户信息");
        }
    }

    @Override
    public PageData<CustomerVO> page(Map<String, Object> params) {
//        long page = Long.parseLong(params.get("page").toString());
//        long limit = Long.parseLong(params.get("limit").toString());
//        Long offset = (page -1) * limit;
//
//        return baseDao.selectPageByParam(params,offset,limit);
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();

        // 如果未传入排序字段 默认采用创建时间作为排序依据
        if(ObjectUtils.isEmpty(params.get("sortField"))){
            params.put("sortField","create_date");
        }

        if(!ObjectUtils.isEmpty(params.get("keyword"))){
            String keyword = params.get("keyword").toString();
            wrapper.and(query -> query.like("customer_name",keyword).or()
                    .like("phone",keyword).or()
                    .like("company",keyword));
        }

        IPage<CustomerEntity> page = baseDao.selectPage(
                getPage(params, params.get("sortField").toString(), (Boolean) params.getOrDefault("isAsc",true)),
                wrapper
        );
        return getPageData(page, CustomerVO.class);

    }

    @Override
    public List<CustomerEntity> listALl() {
        return baseDao.selectList(null);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] ids) {
        //删除
        deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public CustomerEntity loadCustomer(OrderUpdateDTO orderEntity) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerName(orderEntity.getCustomerName());
        // 默认个人用户
        customerEntity.setEnterprise(2);
        customerEntity.setPhone(orderEntity.getPhone());
        customerEntity.setEmail(orderEntity.getEmail());
        customerEntity.setCreator(SecurityUser.getUserId());
        customerEntity.setDealStatus(0);
        insert(customerEntity);

        return customerEntity;
    }


    @Override
    public boolean checkCustomer(String phone){
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();

        wrapper.eq("phone",phone);
        return ObjectUtils.isEmpty(baseDao.selectOne(wrapper));
    }
}
