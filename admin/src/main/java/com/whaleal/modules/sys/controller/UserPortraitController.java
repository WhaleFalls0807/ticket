package com.whaleal.modules.sys.controller;

import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.service.UserPortraitService;
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
    public Result getGrabCount(@RequestParam("startTime")Date startTime,
                               @RequestParam("endTime") Date endTime){

        List list = userPortraitService.findUserGrap(startTime,endTime);
        return new Result();
    }

}
