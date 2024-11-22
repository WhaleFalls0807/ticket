package com.whaleal.modules.oss.cloud;

import com.whaleal.common.exception.OrderException;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.modules.sys.entity.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lyz
 * @desc
 * @create: 2024-11-13 07:18
 **/
@Slf4j
@Service
public class LocalStorageServiceImpl implements LocalStorageService{

    @Value("${oss.path}")
    private String path;

    private String prefix = "/file";

    @Override
    public String uploadFile(Long associationId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new OrderException(OrderExceptionEnum.FILENAME_IS_EMPTY);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(new Date());

        Path dirPath = Paths.get(path, prefix,String.valueOf(associationId),formattedDate);
        Path filePath = dirPath.resolve(originalFilename);

        try {
            // 创建目录（如果不存在）
            if (Files.notExists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 替换已存在的同名文件
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // 保存文件
            file.transferTo(filePath.toFile());

            // 返回相对路径
            return prefix + "/" + associationId + "/" + formattedDate + "/" + originalFilename;
        } catch (IOException e) {
            log.error("文件上传时出现异常：{}", e.getMessage());
            throw new OrderException(5001, "上传文件时出现错误：" + e.getMessage());
        }
    }

}
