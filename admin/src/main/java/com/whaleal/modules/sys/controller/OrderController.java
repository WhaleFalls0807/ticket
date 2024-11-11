package com.whaleal.modules.sys.controller;

import com.whaleal.common.constant.Constant;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.entity.dto.OrderDTO;
import com.whaleal.modules.sys.entity.dto.SysUserDTO;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:23
 **/
@RestController
@RequestMapping("/order")
@Tag(name = "工单管理")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @GetMapping("/all/page")
    @Operation(summary = "分页获取列表")
    @RequiresPermissions("order:list")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "distribute", description = "是否已分配", in = ParameterIn.QUERY, ref = "boolean"),
            @Parameter(name = "sortField", description = "排序字段", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "isAsc", description = "是否升序", in = ParameterIn.QUERY, ref = "boolean"),
    })
    public Result<PageData<SysUserDTO>> pageAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = orderService.page(params);
        return new Result<PageData<SysUserDTO>>().ok(page);
    }


    @PostMapping("/save/byUser")
    @Operation(summary = "普通用户通过门户网站新提一个商标注册申请")
    public Result saveByUser(@RequestBody OrderDTO orderDTO) {
        orderService.createOrder(orderDTO);
        return new Result().ok("");
    }

    @PostMapping("/save/byUser")
    @Operation(summary = "业务员创建商标单子")
    public Result save(@RequestBody OrderEntity orderEntity) {
        orderService.save(orderEntity);
        return new Result().ok("");
    }

    @PostMapping("/distribute")
    @Operation(summary = "分配或抢单")
    @RequiresPermissions("order:distribute")
    public Result distribute() {

        return new Result();
    }

    @PostMapping("/edit/{orderId}")
    @Operation(summary = "编辑更新一个单子")
    @RequiresPermissions("order:edit")
    public Result<PageData<SysUserDTO>> edit(@PathVariable("orderId")String orderId) {
        PageData<SysUserDTO> page = null;

        return new Result<PageData<SysUserDTO>>().ok(page);
    }


}
