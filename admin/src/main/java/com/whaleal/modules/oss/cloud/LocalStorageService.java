package com.whaleal.modules.oss.cloud;

import com.whaleal.modules.sys.entity.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 09:47
 **/
public interface LocalStorageService {

    /**
     * 上传自定义文件到服务器
     * @param associationId
     * @param file
     * @return
     */
    String uploadFile(Long associationId, MultipartFile file);

    /**
     * 删除服务器上上传的文件
     * @param filepath
     */
    void deleteFile(String filepath);

}
