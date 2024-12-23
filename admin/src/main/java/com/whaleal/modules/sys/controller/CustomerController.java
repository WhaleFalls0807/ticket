package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.constant.Constant;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.entity.dto.CustomerDTO;
import com.whaleal.modules.sys.entity.dto.SysUserDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.vo.CustomerVO;
import com.whaleal.modules.sys.service.CustomerService;
import com.whaleal.modules.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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

    private SysUserService sysUserService;

    @Operation(summary = "保存或更新客户基本信息")
    @PostMapping("/saveOrUpdate")
    @LogOperation("保存")
    public Result saveCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.saveOrUpdate(customerDTO);
        return new Result().ok("操作成功");
    }

    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "dealStatus", description = "成交状态", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "sortField", description = "排序字段", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "isAsc", description = "是否升序", in = ParameterIn.QUERY, ref = "boolean"),
    })
    @GetMapping("/page")
    @Operation(summary = "分页展示客户列表")
    public Result<PageData<CustomerVO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        UserDetail user = SecurityUser.getUser();
        boolean isUser = sysUserService.checkAuth(user.getId(),"customer:list:all");
        if(!isUser){
            if(1 != user.getSuperAdmin()){
                params.put("ownerId",user.getId());
            }
        }

        PageData<CustomerVO> page = customerService.page(params);
        page.getList().forEach(s ->{
            if(!ObjectUtils.isEmpty(s.getOwnerUserId()) && 0 != s.getOwnerUserId()){
                SysUserDTO sysUserDTO = sysUserService.get(s.getOwnerUserId());
                if(!ObjectUtils.isEmpty(sysUserDTO)){
                    s.setOwnerUserName(sysUserDTO.getUsername());
                }
            }
        });

        return new Result<PageData<CustomerVO>>().ok(page);
    }

    @Operation(summary = "客户列表",description = "提供给管理员角色筛选客户列表使用")
    @RequiresPermissions("customer:list")
    @GetMapping("/listALl")
    public Result<List<CustomerEntity>> getAllCustomer(){
        List<CustomerEntity> all = customerService.listALl();
        return new Result<List<CustomerEntity>>().ok(all);
    }

    @Operation(summary = "查看客户详情")
    @RequiresPermissions("customer:info")
    @GetMapping("/queryById/{id}")
    public Result<CustomerEntity> queryById(@PathVariable Long id){
        CustomerEntity customerEntity = customerService.selectById(id);
        return new Result<CustomerEntity>().ok(customerEntity);
    }

    @Operation(summary = "客户名是否可用")
    @GetMapping("/name/check")
    public Result<Boolean> checkCustomer(@Parameter(description = "客户名") @RequestParam String customerName){
        boolean exist = customerService.checkCustomer(customerName);
        return new Result<Boolean>().ok(exist);
    }

    @RequiresPermissions("customer:delete")
    @Operation(summary = "删除客户")
    @DeleteMapping("/del")
    public Result deleteByIds(@RequestBody Long[] ids){

        AssertUtils.isArrayEmpty(ids, "id");
        customerService.delete(ids);
        return new Result();
    }
}
