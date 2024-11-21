package com.whaleal.modules.sys.service.impl;

import cn.hutool.core.lang.ObjectId;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.CorDocumentDao;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import com.whaleal.modules.sys.service.CorDocumentService;
import com.whaleal.modules.sys.service.DownloadService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 17:08
 **/
@Service
public class CorDocumentServiceImpl extends BaseServiceImpl<CorDocumentDao, CorDocumentsEntity> implements CorDocumentService {

    private final DownloadService downloadService;

    public CorDocumentServiceImpl(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

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
                new LambdaUpdateWrapper<>()
        );

        return getPageData(page, CorDocumentsEntity.class);
    }

    @Override
    public void download(long id, HttpServletResponse response) {
        CorDocumentsEntity corDocumentsEntity = selectById(id);

        DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.setUsername(SecurityUser.getUser().getUsername());
        downloadRecord.setUserId(SecurityUser.getUserId());
        downloadRecord.setContractNum(corDocumentsEntity.getFileName());

        String filePath = corDocumentsEntity.getFilePath();
        File file = new File(filePath);
        if(!file.exists()){
            throw new OrderException(OrderExceptionEnum.FILE_NOT_EXISTS);
        }

        response.reset();
        response.setContentType("application/octet-stream");
        String filename = corDocumentsEntity.getFileName() + "_" + new ObjectId();
        try {
            InputStream inputStream = new FileInputStream(file);
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));

            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();

            downloadRecord.setSuccess(1);
            downloadService.insert(downloadRecord);
        } catch (Exception e) {
            downloadRecord.setSuccess(2);
            downloadRecord.setFailedReason(e.getMessage());
            downloadService.insert(downloadRecord);
            throw new RuntimeException(e);
        }


    }
}
