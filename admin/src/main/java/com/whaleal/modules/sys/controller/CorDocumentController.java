package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.entity.po.DownloadRecord;
import com.whaleal.modules.sys.service.CorDocumentService;
import com.whaleal.modules.sys.service.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lyz
 * @desc 企业文书接口类
 * @create: 2024-11-19 17:05
 **/
@Tag(name = "企业文书管理")
@Controller
@RequestMapping("/corDocument")
public class CorDocumentController {

    private final CorDocumentService corDocumentService;

    private final DownloadService downloadService;

    public CorDocumentController(CorDocumentService corDocumentService, DownloadService downloadService) {
        this.corDocumentService = corDocumentService;
        this.downloadService = downloadService;
    }

    @PostMapping("/upload")
    @LogOperation("上传")
    @ResponseBody
    @Operation(summary = "上传企业文书")
    public Result upload(@RequestBody CorDocumentDTO corDocumentDTO){
        corDocumentService.uploadFile(corDocumentDTO);
        return new Result();
    }


    @GetMapping("/page")
    @ResponseBody
    @Operation(summary = "分页查看所有的企业文书")
    public Result<PageData<CorDocumentsEntity>> page(@RequestParam Map<String,Object> params){
        PageData<CorDocumentsEntity> page = corDocumentService.page(params);

        page.getList().forEach(s -> {
            Long id = s.getId();
            long count = downloadService.countById(id);

            s.setCount(count);
        });

        return new Result<PageData<CorDocumentsEntity>>().ok(page);
    }


    @GetMapping("/download/page")
    @ResponseBody
    @Operation(summary = "分页查看文件下载记录")
    public Result<PageData<DownloadRecord>> pageDownload(@RequestParam Map<String,Object> params){
        PageData<DownloadRecord> page = downloadService.page(params);

        return new Result<PageData<DownloadRecord>>().ok(page);
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "下载企业文书")
    public void download(@PathVariable("fileId") Long fileId,
                           HttpServletResponse response){
        corDocumentService.download(fileId,response);
    }

    @RequiresPermissions("customer:delete")
    @Operation(summary = "删除企业文书")
    @ResponseBody
    @PostMapping("/del")
    public Result deleteByIds(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        corDocumentService.delete(ids);
        return new Result();
    }

}
