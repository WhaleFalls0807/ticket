package com.whaleal.modules.sys.controller;

import com.whaleal.common.constant.Constant;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.sys.entity.po.NotificationEntity;
import com.whaleal.modules.sys.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 13:37
 **/
@Tag(name = "通知中心")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "分页获取所有的站内信通知")
    @GetMapping("/page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "read", description = "0:未读 1：已读 2：全部", in = ParameterIn.QUERY, ref = "int"),
    })
    public Result<PageData<NotificationEntity>> pageAll(@RequestParam Map<String,Object> params){
        PageData<NotificationEntity> pageData = notificationService.page(params);
        return new Result<PageData<NotificationEntity>>().ok(pageData);
    }


    @Operation(summary = "标记站内信已读")
    @PostMapping("/read/{id}")
    public Result<String> readById(@PathVariable Long id){
        notificationService.read(id);
        return new Result<String>().ok("操作成功");
    }

    @Operation(summary = "标记站内信已读")
    @PostMapping("/read")
    public Result<String> readUnreadNotification(){
        notificationService.read(null);
        return new Result<String>().ok("操作成功");
    }

    @Operation(summary = "删除站内信")
    @PostMapping("/delete/{id}")
    public Result<String> deleteNotification(@PathVariable("id") Long id){
        notificationService.deleteById(id);
        return new Result<String>().ok("删除成功");
    }


    @Operation(summary = "删除所有已读站内信")
    @PostMapping("/delete")
    public Result<String> deleteAllRead(){
        notificationService.deleteAllRead();
        return new Result<String>().ok("删除成功");
    }
}
