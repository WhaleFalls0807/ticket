package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.dto.SysUserDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import com.whaleal.modules.sys.service.CorDocumentService;
import com.whaleal.modules.sys.service.DownloadService;
import com.whaleal.modules.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lyz
 * @desc 企业文书接口类
 * @create: 2024-11-19 17:05
 **/
@Tag(name = "企业文书管理")
@RestController
@RequestMapping("/corDocument")
public class CorDocumentController {

    private final CorDocumentService corDocumentService;

    private final DownloadService downloadService;

    private final SysUserService sysUserService;

    public CorDocumentController(CorDocumentService corDocumentService, DownloadService downloadService, SysUserService sysUserService) {
        this.corDocumentService = corDocumentService;
        this.downloadService = downloadService;
        this.sysUserService = sysUserService;
    }

    @PostMapping("/upload")
    @LogOperation("上传")
    @Operation(summary = "上传企业文书")
    public Result upload(@RequestBody CorDocumentDTO corDocumentDTO){
        corDocumentService.uploadFile(corDocumentDTO);
        return new Result();
    }


    @GetMapping("/page")
    @Operation(summary = "分页查看所有的企业文书")
    public Result<PageData<CorDocumentsEntity>> page(@RequestParam Map<String,Object> params){
        PageData<CorDocumentsEntity> page = corDocumentService.page(params);

        page.getList().forEach(s -> {
            Long id = s.getId();
            long count = downloadService.countById(id);
            s.setCount(count);

            Long creator = s.getCreator();
            SysUserDTO sysUserDTO = sysUserService.get(creator);
            s.setCreateName(sysUserDTO.getUsername());
        });

        return new Result<PageData<CorDocumentsEntity>>().ok(page);
    }


    @GetMapping("/download/page")
    @Operation(summary = "分页查看文件下载记录")
    public Result<PageData<DownloadRecord>> pageDownload(@RequestParam Map<String,Object> params){
        PageData<DownloadRecord> page = downloadService.page(params);

        return new Result<PageData<DownloadRecord>>().ok(page);
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "下载企业文书")
    public Result download(@PathVariable("fileId") Long fileId){
       return new Result().ok( corDocumentService.download(fileId));
    }

    @Operation(summary = "删除企业文书")
    @DeleteMapping("/del")
    public Result deleteByIds(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        corDocumentService.delete(ids);
        return new Result();
    }

}
