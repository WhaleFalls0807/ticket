package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.CustomerDao;
import com.whaleal.modules.sys.entity.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
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
            // 新增
            if(ObjectUtils.isEmpty(entity.getOwnerUserId())){
                entity.setOwnerUserId(SecurityUser.getUserId());
            }
            insert(entity);
            log.info("新增一条客户信息");
        }else {
            // 更新
            updateById(entity);
            log.info("修改了一条客户信息");
        }
    }

    @Override
    public PageData<CustomerEntity> page(Map<String, Object> params) {
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();

        // 如果未传入排序字段 默认采用创建时间作为排序依据
        if(ObjectUtils.isEmpty(params.get("sortField"))){
            params.put("sortField","update_time");
        }

        if(!ObjectUtils.isEmpty(params.get("keyword"))){
            String keyword = params.get("keyword").toString();
            wrapper.and(query -> query.like("customer_name",keyword).or()
                    .like("phone",keyword).or()
                    .like("contact_name",keyword));
        }

        IPage<CustomerEntity> page = baseDao.selectPage(
                getPage(params, "sort", (Boolean) params.getOrDefault("isAsc",true)),
                wrapper
        );

        return getPageData(page, CustomerEntity.class);
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
    public boolean checkCustomer(String customerName) {
        QueryWrapper<CustomerEntity> qw = new QueryWrapper<>();
        qw.eq("customer_name",customerName);
        CustomerEntity customerEntity = baseDao.selectOne(qw);

        return ObjectUtils.isEmpty(customerEntity);
    }
}
