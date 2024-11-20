package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.entity.dto.CorDocumentDTO;
import com.whaleal.modules.sys.entity.po.CorDocumentsEntity;
import com.whaleal.modules.sys.service.CorDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    public CorDocumentController(CorDocumentService corDocumentService) {
        this.corDocumentService = corDocumentService;
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
        return new Result<PageData<CorDocumentsEntity>>().ok(corDocumentService.page(params));
    }


    @PostMapping("/download")
    @Operation(summary = "下载企业文书")
    public Result download(@RequestBody Map<String, List<Long>> data,
                                                         HttpServletResponse response){
        List<Long> ids = data.get("ids");
        corDocumentService.download(ids,response);
        return new Result();
    }

}
