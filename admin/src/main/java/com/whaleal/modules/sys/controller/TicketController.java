package com.whaleal.modules.sys.controller;

import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.dto.SysUserDTO;
import com.whaleal.modules.sys.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/ticket")
@Tag(name = "工单管理")
@AllArgsConstructor
public class TicketController {

    private TicketService ticketService;

    @GetMapping("/all/page")
    @Operation(summary = "分页获取列表")
    @RequiresPermissions("ticket:list")
    public Result<PageData<SysUserDTO>> pageAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = null;

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    @PostMapping("/distribute")
    @Operation(summary = "分配或抢单")
    @RequiresPermissions("ticket:distribute")
    public Result<PageData<SysUserDTO>> distribute(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = null;

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    @PostMapping("/edit/{ticketId}")
    @Operation(summary = "编辑更新一个单子")
    @RequiresPermissions("ticket:edit")
    public Result<PageData<SysUserDTO>> edit(@PathVariable("ticketId")String ticketId) {
        PageData<SysUserDTO> page = null;

        return new Result<PageData<SysUserDTO>>().ok(page);
    }


}
