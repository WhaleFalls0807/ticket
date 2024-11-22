package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.modules.sys.dao.DownloadDao;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import com.whaleal.modules.sys.service.DownloadService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 18:00
 **/
@Service
public class DownloadServiceImpl extends BaseServiceImpl<DownloadDao, DownloadRecord> implements DownloadService {


    @Override
    public long countById(Long id) {

        LambdaUpdateWrapper<DownloadRecord> qw = new LambdaUpdateWrapper<>();

        qw.eq(DownloadRecord::getAssociationId,id);

        return baseDao.selectCount(qw);
    }

    @Override
    public PageData<DownloadRecord> page(Map<String, Object> params) {

        IPage<DownloadRecord> page = baseDao.selectPage(
                getPage(params, "create_date", false),
                new LambdaUpdateWrapper<>()
        );

        return getPageData(page, DownloadRecord.class);
    }
}
