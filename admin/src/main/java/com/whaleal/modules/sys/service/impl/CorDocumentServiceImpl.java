package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 17:08
 **/
@Slf4j
@Service
public class CorDocumentServiceImpl extends BaseServiceImpl<CorDocumentDao, CorDocumentsEntity> implements CorDocumentService {

    @Value("${oss.path}")
    private String basePath ;

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
                getPage(params, "create_date", false),
                new LambdaUpdateWrapper<>()
        );

        return getPageData(page, CorDocumentsEntity.class);
    }

    @Override
    public void download(long id, HttpServletResponse response) {
        CorDocumentsEntity corDocumentsEntity = selectById(id);

        DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.setAssociationId(id);
        downloadRecord.setUsername(SecurityUser.getUser().getUsername());
        downloadRecord.setUserId(SecurityUser.getUserId());
        downloadRecord.setContractNum(corDocumentsEntity.getFileName());

        String filePath = corDocumentsEntity.getFilePath();
        File file = new File(basePath + filePath);
        if(!file.exists()){
            log.error("文件不存在");
            throw new RuntimeException("文件不存在");
        }

        response.reset();
        response.setContentType("application/octet-stream");
        String filename = UUID.randomUUID().toString().replace("-", "") + "_" + corDocumentsEntity.getFileName();
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
            downloadRecord.setFailedReason("系统异常");
            downloadService.insert(downloadRecord);
            handleError(response,e.getMessage(),5002);
        }
    }

    @Override
    public void delete(Long[] ids) {
        deleteBatchIds(Arrays.asList(ids));
    }

    private void handleError(HttpServletResponse response, String message, int statusCode) {
        try {
//            response.reset();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(statusCode);

            // 构建错误响应对象
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", statusCode);
            errorResponse.put("message", message);

            // 返回错误响应
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error("响应错误时异常: {}", e.getMessage());
        }
    }

}
