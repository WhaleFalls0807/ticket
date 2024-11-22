package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.po.DownloadRecord;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 18:00
 **/
public interface DownloadService extends BaseService<DownloadRecord> {
    long countById(Long id);

    PageData<DownloadRecord> page(Map<String, Object> params);
}
