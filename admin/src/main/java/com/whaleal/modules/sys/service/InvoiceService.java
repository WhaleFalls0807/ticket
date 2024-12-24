package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.InvoiceDTO;
import com.whaleal.modules.sys.entity.po.InvoiceEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 16:56
 **/
public interface InvoiceService extends BaseService<InvoiceEntity> {

    /**
     * 保存 / 更新记录
     * @param invoiceDTO
     */
    void saveOrUpdate(InvoiceDTO invoiceDTO);


    /**
     * 分页查看所有
     * @param params
     * @return
     */
    PageData<InvoiceEntity> page(Map<String, Object> params);

    /**
     * 删除开票记录
     * @param id
     */
    void delete(Long id);


    List<Map<String, Object>> findUserInvoice(Date start, Date end);
}
