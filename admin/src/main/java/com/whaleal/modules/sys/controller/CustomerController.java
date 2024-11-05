package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.sys.entity.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:48
 **/
@RestController
@RequestMapping("/customer")
@Tag(name = "客户管理")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @Operation(summary = "保存或更新客户")
    @PostMapping("/saveOrUpdate")
    @LogOperation("保存")
    @RequiresPermissions("customer:save")
    public Result saveCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.saveOrUpdate(customerDTO);
        return new Result().ok("操作成功");
    }

    @GetMapping("/page")
    @Operation(summary = "分页展示客户列表")
    public Result<PageData<CustomerEntity>> page(){
        PageData<CustomerEntity> page = customerService.page();
        return new Result<PageData<CustomerEntity>>().ok(page);
    }

    @PostMapping("/listALl")
    public Result<List<CustomerEntity>> getAllCustomer(){
        List<CustomerEntity> all = customerService.listALl();
        return new Result<>();
    }

    @PostMapping("/del")
    public Result deleteByIds(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        customerService.delete(ids);
        return new Result();
    }
}
