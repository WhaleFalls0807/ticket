package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.CorDocumentDao;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.SysDictDataEntity;
import com.whaleal.modules.sys.service.CorDocumentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 17:08
 **/
@Service
public class CorDocumentServiceImpl extends BaseServiceImpl<CorDocumentDao, CorDocumentsEntity> implements CorDocumentService {


    @Override
    public void uploadFile(CorDocumentDTO corDocumentDTO) {
        CorDocumentsEntity corDocumentsEntity = ConvertUtils.sourceToTarget(corDocumentDTO, CorDocumentsEntity.class);

        corDocumentsEntity.setCreator(SecurityUser.getUserId());
        baseDao.insert(corDocumentsEntity);
    }

    @Override
    public PageData<CorDocumentsEntity> page(Map<String,Object> params) {
        IPage<CorDocumentsEntity> page = baseDao.selectPage(
                getPage(params, "sort", true),
                new LambdaUpdateWrapper<CorDocumentsEntity>()
        );

        return getPageData(page, CorDocumentsEntity.class);
    }

    @Override
    public void download(List<Long> ids, HttpServletResponse response) {

    }
}
