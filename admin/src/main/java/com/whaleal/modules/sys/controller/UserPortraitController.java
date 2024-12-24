package com.whaleal.modules.sys.controller;

import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.service.UserPortraitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 23:25
 **/
@Tag(name = "用户画像接口")
@RestController
@RequestMapping("/userPortrait")
public class UserPortraitController {


    private final UserPortraitService userPortraitService;

    public UserPortraitController(UserPortraitService userPortraitService) {
        this.userPortraitService = userPortraitService;
    }

    @GetMapping("/grapedCount")
    @Operation(summary = "获取抢单数")
    public Result getGrabCount(@RequestParam("startTime")Long  startTime,
                               @RequestParam("endTime") Long  endTime){

        Date start = new Date(startTime);
        Date end = new Date(endTime);
        return new Result().ok(userPortraitService.findUserGrap(start,end));
    }

    @GetMapping("/wechatCount")
    @Operation(summary = "获取沟通次数")
    public Result getWechat(@RequestParam("startTime")Long  startTime,
                               @RequestParam("endTime") Long  endTime){

        Date start = new Date(startTime);
        Date end = new Date(endTime);

        return new Result().ok(userPortraitService.getWeChatCount(start,end));
    }

    @GetMapping("/totalInvoice")
    @Operation(summary = "获取开票金额")
    public Result getInvoice(@RequestParam("startTime")Long  startTime,
                               @RequestParam("endTime") Long  endTime){

        Date start = new Date(startTime);
        Date end = new Date(endTime);

        return new Result().ok(userPortraitService.listInvoicePrice(start,end));
    }

}
