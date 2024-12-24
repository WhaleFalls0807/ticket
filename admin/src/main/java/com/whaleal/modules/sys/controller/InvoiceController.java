package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.constant.Constant;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.entity.dto.InvoiceDTO;
import com.whaleal.modules.sys.entity.po.InvoiceEntity;
import com.whaleal.modules.sys.service.InvoiceService;
import com.whaleal.modules.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-13 16:50
 **/
@RestController
@RequestMapping("/invoice")
@Tag(name = "开票管理")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final SysUserService sysUserService;


    public InvoiceController(InvoiceService invoiceService, SysUserService sysUserService) {
        this.invoiceService = invoiceService;
        this.sysUserService = sysUserService;
    }

    @LogOperation("保存/保存开票记录")
    @Operation(summary = "修改或新增一个invoice")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody InvoiceDTO invoiceDTO){
        invoiceService.saveOrUpdate(invoiceDTO);
        return new Result();
    }


    @Operation(summary = "保存或新增一个invoice")
    @GetMapping("/page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = Constant.ORDER_FIELD, description = "排序字段", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = Constant.ORDER, description = "是否升序", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "startDate", description = "开始时间", in = ParameterIn.QUERY, ref = "Date"),
            @Parameter(name = "endDate", description = "结束时间", in = ParameterIn.QUERY, ref = "Date"),
    })
    public Result<PageData<InvoiceEntity>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        UserDetail user = SecurityUser.getUser();
        if(!(user.getSuperAdmin() == 1)){
            // 非超级管理员
            if(!sysUserService.checkAuth(user.getId(), "invoice:list:all")){
                // 没有查看所有invoice的权限 只能查看自己的invoice列表
                params.put("ownerId",user.getId());
            }
        }
        PageData<InvoiceEntity> page = invoiceService.page(params);

        return new Result<PageData<InvoiceEntity>>().ok(page);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除")
    @LogOperation("删除")
    @RequiresPermissions("invoice:delete")
    public Result delete(@PathVariable("id") Long id) {
        //效验数据
        AssertUtils.isNull(id, "id");
        invoiceService.delete(id);
        return new Result();
    }
}
