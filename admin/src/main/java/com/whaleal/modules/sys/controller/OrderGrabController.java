package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.entity.po.UserGrabConfigEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;
import com.whaleal.modules.sys.service.UserGrabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-19 16:44
 **/
@Tag(name = "用户抢单配置")
@RestController
@RequestMapping("/orderGrab")
public class OrderGrabController {

    private UserGrabService userGrabService;

    public OrderGrabController(UserGrabService userGrabService) {
        this.userGrabService = userGrabService;
    }

    @PostMapping("/config/save")
    @Operation(summary = "修改/保存用户抢单设置")
    @LogOperation("更新")
    @RequiresPermissions("order:grab:save")
    public Result saveOrUpdate(@RequestBody UserGrabConfigEntity userGrabConfigEntity) {
        userGrabService.saveOrUpdate(userGrabConfigEntity);
        return new Result();
    }

    @GetMapping("/config/{userId}")
    @Operation(summary = "查询客户抢单配置")
    @RequiresPermissions("order:grab:info")
    public Result<UserGrabConfigEntity> updateGrabConfig(@PathVariable Long userId) {
        return new Result<UserGrabConfigEntity>().ok(userGrabService.findByUserId(userId));
    }

    @GetMapping("/count")
    @Operation(summary = "获取抢单数/总数")
    public Result<OrderGrabVO> queryCount() {
        Long userId = SecurityUser.getUserId();
        return new Result<OrderGrabVO>().ok(userGrabService.findCountByUserId(userId));
    }
}
