package com.whaleal.modules.oss.cloud;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 09:47
 **/
public interface LocalStorageService {

    String uploadFile(Long associationId, MultipartFile file);
}
