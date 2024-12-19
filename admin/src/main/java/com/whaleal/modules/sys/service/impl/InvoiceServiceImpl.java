package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.InvoiceDao;
import com.whaleal.modules.sys.entity.dto.InvoiceDTO;
import com.whaleal.modules.sys.entity.po.InvoiceEntity;
import com.whaleal.modules.sys.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 16:58
 **/
@Service
public class InvoiceServiceImpl extends BaseServiceImpl<InvoiceDao, InvoiceEntity> implements InvoiceService {

    @Override
    public void saveOrUpdate(InvoiceDTO invoiceDTO) {
        InvoiceEntity invoiceEntity = ConvertUtils.sourceToTarget(invoiceDTO, InvoiceEntity.class);

        if(ObjectUtils.isEmpty(invoiceDTO.getId())){
            invoiceEntity.setCreatorName(SecurityUser.getUser().getUsername());
            baseDao.insert(invoiceEntity);
        }else {
            baseDao.updateById(invoiceEntity);
        }
    }

    @Override
    public PageData<InvoiceEntity> page(Map<String, Object> params) {
        LambdaQueryWrapper<InvoiceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(!ObjectUtils.isEmpty(params.get("ownerId"))){
            long userid = Long.parseLong(params.get("ownerId").toString());
            lambdaQueryWrapper.eq(InvoiceEntity::getCreator,userid);
        }

        if(!ObjectUtils.isEmpty(params.get("customerId"))){
            long userid = Long.parseLong(params.get("customerId").toString());
            lambdaQueryWrapper.eq(InvoiceEntity::getCustomerName,userid);
        }

        if(ObjectUtils.isEmpty(params.get("sortField"))){
            params.put("sortField","create_date");
        }

        if(ObjectUtils.isEmpty(params.get("isAsc"))){
            params.put("isAsc",false);
        }

        IPage<InvoiceEntity> invoiceEntityIPage = baseDao.selectPage(getPage(params, params.get("sortField").toString(), (boolean) params.get("isAsc")),
                lambdaQueryWrapper);

        return getPageData(invoiceEntityIPage, InvoiceEntity.class);
    }

    @Override
    public void delete(Long id) {
        baseDao.deleteById(id);
    }
}
