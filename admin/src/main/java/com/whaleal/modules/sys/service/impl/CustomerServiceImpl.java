package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.sys.dao.CustomerDao;
import com.whaleal.modules.sys.dto.CustomerDTO;
import com.whaleal.modules.sys.dto.SysDictDataDTO;
import com.whaleal.modules.sys.entity.CustomerEntity;
import com.whaleal.modules.sys.entity.SysDictDataEntity;
import com.whaleal.modules.sys.entity.SysDictTypeEntity;
import com.whaleal.modules.sys.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            insert(entity);
            log.info("新增一条客户信息");
        }else {
            // 更新
            updateById(entity);
        }
    }

    @Override
    public List<CustomerEntity> listALl() {

        return null;
    }

    @Override
    public PageData<CustomerEntity> page() {
        Map<String, Object> params = new HashMap<>();
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();

        IPage<CustomerEntity> page = baseDao.selectPage(
                getPage(params, "sort", true),
                wrapper
        );

        return getPageData(page, CustomerEntity.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] ids) {
        //删除
        deleteBatchIds(Arrays.asList(ids));

    }
}
