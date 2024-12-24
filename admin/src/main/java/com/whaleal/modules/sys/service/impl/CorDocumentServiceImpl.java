package com.whaleal.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whaleal.common.page.PageData;
import com.whaleal.common.service.impl.BaseServiceImpl;
import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.oss.cloud.LocalStorageService;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.dao.CorDocumentDao;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.dto.DownloadDTO;
import com.whaleal.modules.sys.entity.dto.NotificationDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import com.whaleal.modules.sys.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

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

    private final LocalStorageService localStorageService;

    private final NotificationService notificationService;

    private final SysRoleUserService sysRoleUserService;

    public CorDocumentServiceImpl(DownloadService downloadService, LocalStorageService localStorageService, NotificationService notificationService, SysUserService sysUserService, SysRoleUserService sysRoleUserService) {
        this.downloadService = downloadService;
        this.localStorageService = localStorageService;
        this.notificationService = notificationService;
        this.sysRoleUserService = sysRoleUserService;
    }

    @Override
    public void uploadFile(CorDocumentDTO corDocumentDTO) {
        CorDocumentsEntity corDocumentsEntity = ConvertUtils.sourceToTarget(corDocumentDTO, CorDocumentsEntity.class);
        if(ObjectUtils.isEmpty(corDocumentDTO.getId())){
            corDocumentsEntity.setCreator(SecurityUser.getUserId());
            baseDao.insert(corDocumentsEntity);
        }else {
            corDocumentsEntity.setUpdater(SecurityUser.getUser().getUsername());
            System.out.println(corDocumentDTO.getId());

            int i = baseDao.updateById(corDocumentsEntity);
            System.out.println(i);
        }

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
    public String download(DownloadDTO downloadDTO) {
        Long id = downloadDTO.getFileId();

        CorDocumentsEntity corDocumentsEntity = selectById(id);

        DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.setAssociationId(id);
        downloadRecord.setUsername(SecurityUser.getUser().getUsername());
        downloadRecord.setUserId(SecurityUser.getUserId());

        if(StringUtils.hasText(downloadDTO.getCustomerName()) || !ObjectUtils.isEmpty(downloadDTO.getCustomerId())){
            downloadRecord.setCustomerId(downloadDTO.getCustomerId());
            downloadRecord.setCustomerName(downloadDTO.getCustomerName());
        }

        String filePath = corDocumentsEntity.getFilePath();
        File file = new File(basePath + filePath);

        String filename;
        if(!file.exists()){
            downloadRecord.setSuccess(2);
            downloadRecord.setFailedReason("文件不存在");
            filename = "文件不存在";
        }else {
            String contractId ;
            if(StringUtils.hasText(downloadDTO.getContractNum())){
                contractId = downloadDTO.getContractNum();
            }else {
                contractId = UUID.randomUUID().toString().replace("-", "") ;
            }

            downloadRecord.setSuccess(1);
            downloadRecord.setContractNum(contractId);
            filename = downloadRecord.getCustomerName() + "_" + SecurityUser.getUser().getUsername() + "_" + contractId + "_" +  corDocumentsEntity.getFileName();
            downloadRecord.setFilename(filename);

            List<Long> userIds = sysRoleUserService.getRoleUserId("管理员");
            if(userIds.size() > 0){
                userIds.remove(SecurityUser.getUserId());
                if(userIds.size() > 0){
                    notificationService.createNotification(new NotificationDTO(id,userIds,"下载了文件【" + filename + "】" ,11));
                }
            }

        }
        downloadService.insert(downloadRecord);
        return filename;
//        try {
//            InputStream inputStream = new FileInputStream(file);
//            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
//
//            ServletOutputStream outputStream = response.getOutputStream();
//            byte[] b = new byte[1024];
//            int len;
//            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
//            while ((len = inputStream.read(b)) > 0) {
//                outputStream.write(b, 0, len);
//            }
//
//            inputStream.close();
//            downloadRecord.setSuccess(1);
//            downloadService.insert(downloadRecord);
//        } catch (Exception e) {
//            downloadRecord.setSuccess(2);
//            downloadRecord.setFailedReason("系统异常");
//            downloadService.insert(downloadRecord);
//            handleError(response,e.getMessage(),5002);
//        }
    }

    @Transactional
    @Override
    public void delete(Long[] ids) {
        LambdaQueryWrapper<CorDocumentsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CorDocumentsEntity::getId,Arrays.asList(ids));

        List<CorDocumentsEntity> corDocumentsEntities = baseDao.selectList(queryWrapper);
        for (CorDocumentsEntity corDocumentsEntity : corDocumentsEntities) {
            String filePath = corDocumentsEntity.getFilePath();
            localStorageService.deleteFile(filePath);
        }

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
