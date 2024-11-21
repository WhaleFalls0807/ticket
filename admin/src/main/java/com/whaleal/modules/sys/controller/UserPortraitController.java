package com.whaleal.modules.sys.controller;

import com.whaleal.common.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 23:25
 **/
@Tag(name = "用户画像接口")
@RestController
@RequestMapping("/userPortrait")
public class UserPortraitController {


    @GetMapping()
    public Result getGrabCount(){

        return new Result();
    }

}
