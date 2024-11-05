package com.whaleal.modules.sys.controller;

import com.whaleal.common.constant.Constant;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.service.ActivityService;
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
 * @create: 2024-10-29 21:58
 **/
@RestController
@RequestMapping("/activity")
@Tag(name = "跟进管理")
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/list")
    @Operation(summary = "查看某个业务的跟机列表")
    @RequiresPermissions("activity:list")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "associationId", description = "关联客户ID", in = ParameterIn.QUERY, ref = "String")
    })
    public Result<PageData<ActivityEntity>> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<ActivityEntity> list = activityService.listAllById(params);
        return new Result<PageData<ActivityEntity>>().ok(list);
    }

    @PostMapping("/create")
    @Operation(summary = "创建一条跟进记录")
    @RequiresPermissions("activity:create")
    public Result createActivity(@RequestBody ActivityDTO activityDTO){
        activityService.createActivity(activityDTO);
        return new Result().ok("创建成功");
    }

    @PostMapping("/update")
    @Operation(summary = "更新跟进记录")
    @RequiresPermissions("activity:update")
    public Result updateActivity(@RequestBody ActivityEntity activityEntity){
        activityService.updateActivity(activityEntity);
        return new Result().ok("创建成功");
    }

    @PostMapping("/delete/{activityId}")
    @Operation(summary = "删除一条跟进记录")
    @RequiresPermissions("activity:create")
    public Result deleteActivity(@PathVariable("activityId") String activityId){
        activityService.deleteById(activityId);
        return new Result().ok("删除成功");
    }

}
