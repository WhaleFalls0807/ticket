/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.whaleal.modules.oss.controller;

import cn.hutool.core.io.file.FileNameUtil;
import com.whaleal.common.constant.Constant;
import com.whaleal.common.exception.ErrorCode;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.JsonUtils;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.oss.cloud.CloudStorageConfig;
import com.whaleal.modules.oss.cloud.LocalStorageService;
import com.whaleal.modules.oss.cloud.OSSFactory;
import com.whaleal.modules.oss.entity.SysOssEntity;
import com.whaleal.modules.oss.service.SysOssService;
import com.whaleal.modules.sys.entity.vo.FileUploadVO;
import com.whaleal.modules.sys.service.SysParamsService;
import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.validator.ValidatorUtils;
import com.whaleal.common.validator.group.AliyunGroup;
import com.whaleal.common.validator.group.QcloudGroup;
import com.whaleal.common.validator.group.QiniuGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
@Tag(name = "文件上传")
@AllArgsConstructor
public class SysOssController {
    private final SysOssService sysOssService;
    private final SysParamsService sysParamsService;

    private final LocalStorageService localStorageService;

    private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;

    @GetMapping("page")
    @Operation(summary = "分页")
    @RequiresPermissions("sys:oss:all")
    public Result<PageData<SysOssEntity>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysOssEntity> page = sysOssService.page(params);

        return new Result<PageData<SysOssEntity>>().ok(page);
    }

    @GetMapping("info")
    @Operation(summary = "云存储配置信息")
    @RequiresPermissions("sys:oss:all")
    public Result<CloudStorageConfig> info() {
        CloudStorageConfig config = sysParamsService.getValueObject(KEY, CloudStorageConfig.class);

        return new Result<CloudStorageConfig>().ok(config);
    }

    @PostMapping
    @Operation(summary = "保存云存储配置信息")
    @LogOperation("保存云存储配置信息")
    @RequiresPermissions("sys:oss:all")
    public Result saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == Constant.CloudService.QINIU.getValue()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        sysParamsService.updateValueByCode(KEY, JsonUtils.toJsonString(config));

        return new Result();
    }

    @PostMapping("upload")
    @Operation(summary = "上传文件")
    @RequiresPermissions("sys:oss:all")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }

        //上传文件
        String suffix = FileNameUtil.getSuffix(file.getOriginalFilename());
        String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        sysOssService.insert(ossEntity);

        Map<String, Object> data = new HashMap<>(1);
        data.put("src", url);

        return new Result<Map<String, Object>>().ok(data);
    }

    /**
     * 自定义文件上传
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/{associationId}")
    @Operation(summary = "上传文件 - 本次开发使用上传接口")
    public Result<String> uploadLocal(@PathVariable("associationId") Long associationId,
                                            @RequestParam("file") MultipartFile file)  {
        if (file.isEmpty()) {
            return new Result<String>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        return new Result<String>().ok(localStorageService.uploadFile(associationId, file));
    }


    /**
     * 自定义文件上传
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    @Operation(summary = "删除文件")
    public Result<String> deleteFile(@RequestParam String filepath)  {
        localStorageService.deleteFile(filepath);
        return new Result<String>();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @LogOperation("删除")
    @RequiresPermissions("sys:oss:all")
    public Result delete(@RequestBody Long[] ids) {
        sysOssService.deleteBatchIds(Arrays.asList(ids));
        return new Result();
    }

}
