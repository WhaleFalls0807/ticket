package com.whaleal.modules.sys.service;

import com.whaleal.common.page.PageData;
import com.whaleal.common.service.BaseService;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 17:08
 **/
public interface CorDocumentService extends BaseService<CorDocumentsEntity> {

    /**
     * 上传一个企业文书
     * @param corDocumentDTO
     */
    void uploadFile(CorDocumentDTO corDocumentDTO);

    /**
     * 分页展示所有的文书
     *
     * @param corDocumentDTO
     * @return
     */
    PageData<CorDocumentsEntity> page(Map<String,Object> params);


    /**
     * 根据ID下载企业文书
     * @param ids
     * @param response
     */
    void download(List<Long> ids, HttpServletResponse response);

}
