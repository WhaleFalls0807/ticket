package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.constant.Constant;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.entity.dto.OrderCommitDTO;
import com.whaleal.modules.sys.entity.dto.OrderDTO;
import com.whaleal.modules.sys.entity.dto.OrderReviewDTO;
import com.whaleal.modules.sys.entity.dto.OrderUpdateDTO;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.vo.OrderVO;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.CustomerService;
import com.whaleal.modules.sys.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.ObjectUtils;
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

    private final OrderService orderService;

    private final CustomerService customerService;


    @GetMapping("/all/page")
    @Operation(summary = "分页获取列表")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "orderStatus", description = "order状态", in = ParameterIn.QUERY, ref = "int"),
            @Parameter(name = "deal", description = "0：公海 1：待成单 2：已成单", in = ParameterIn.QUERY, ref = "int",required = true),
            @Parameter(name = "sortField", description = "排序字段", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "isAsc", description = "是否升序", in = ParameterIn.QUERY, ref = "boolean"),
            @Parameter(name = "startDate", description = "开始时间", in = ParameterIn.QUERY, ref = "Date"),
            @Parameter(name = "endDate", description = "结束时间", in = ParameterIn.QUERY, ref = "Date"),
    })
    public Result<PageData<OrderVO>> pageAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        int deal = Integer.parseInt(params.get("deal").toString());

        if(deal == 0){
            //判断是否是查询公海order，公海查询不限制权限
            params.remove("ownerId");
        }else {
            UserDetail user = SecurityUser.getUser();
            if(user.getSuperAdmin() != 1){
                //普通用户只能查看自己的order
                params.put("ownerId",user.getId());
            }
        }

        PageData<OrderVO> page = orderService.page(params);
        return new Result<PageData<OrderVO>>().ok(page);
    }


    @GetMapping("/review/all/page")
    @Operation(summary = "分页获取审核列表")
    @RequiresPermissions("order:review")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "reviewType", description = "审核类别 0：全部 1：待审核 2：已审核", in = ParameterIn.QUERY, ref = "int",required = true),
            @Parameter(name = "sortField", description = "排序字段", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "isAsc", description = "是否升序", in = ParameterIn.QUERY, ref = "boolean"),
            @Parameter(name = "startDate", description = "开始时间", in = ParameterIn.QUERY, ref = "Date"),
            @Parameter(name = "endDate", description = "结束时间", in = ParameterIn.QUERY, ref = "Date"),
    })
    public Result<PageData<OrderVO>> pageAllReview(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        params.put("deal",1);
        PageData<OrderVO> page = orderService.page(params);
        return new Result<PageData<OrderVO>>().ok(page);
    }

    /**
     *  todo 加一些限流或其他安全机制 防止单设备攻击
     * @param orderDTO
     * @return
     */
    @PostMapping("/save/byUser")
    @Operation(summary = "普通用户通过门户网站新提一个商标注册申请")
    public Result<String> saveByUser(@RequestBody OrderDTO orderDTO) {
        orderService.saveSimpleOrder(orderDTO,false);

        return new Result<String>().ok("创建成功");
    }

    @PostMapping("/save/inner")
    @Operation(summary = "业务员创建单子")
    @RequiresPermissions("order:create")
    public Result<String> saveInner(@RequestBody OrderDTO orderDTO ) {
        orderService.saveSimpleOrder(orderDTO,true);
        return new Result<String>().ok("创建成功");
    }

    @PostMapping("/distribute/{orderId}")
    @Operation(summary = "管理员分配单子")
    @RequiresPermissions("order:distribute")
    public Result<String> distributeOrder(@PathVariable Long orderId,
                                          @RequestBody Map<String,Long> data) {
        Long userId = data.get("userId");
        orderService.distributeOrder(orderId,userId);
        return new Result<String>().ok("分配成功");
    }

    @PostMapping("/choose/{orderId}")
    @Operation(summary = "业务员抢单子")
    @RequiresPermissions("order:choose")
    public Result chooseOrder(@PathVariable Long orderId) {
        Long userId = SecurityUser.getUserId();
        orderService.distributeOrder(orderId,userId);
        return new Result();
    }

    @PostMapping("/info/add")
    @Operation(summary = "补充单子信息")
    @RequiresPermissions("order:edit")
    public Result addInformation(@RequestBody OrderUpdateDTO orderUpdateDTO) {
        OrderEntity orderEntity = orderService.selectById(orderUpdateDTO.getId());
        if(null == orderEntity){
            return new Result().error(OrderExceptionEnum.ORDER_NOT_EXISTS.getCode(),OrderExceptionEnum.ORDER_NOT_EXISTS.getMsg());
        }

        if(ObjectUtils.isEmpty(orderUpdateDTO.getCustomerId())){
            // 如果客户不存在，就根据客户信息创建一个客户
            // todo 是否更改客户状态
            CustomerEntity customerEntity = customerService.loadCustomer(orderUpdateDTO);
            orderUpdateDTO.setCustomerId(customerEntity.getId());
        }
        orderService.addInformation(orderEntity,orderUpdateDTO);
        return new Result();
    }

    @PostMapping("/commit")
    @Operation(summary = "业务员提交单子")
    @RequiresPermissions("order:create")
    public Result<String> commitForReview(@RequestBody OrderCommitDTO orderCommitDTO ) {
        orderService.commit(orderCommitDTO);
        return new Result<String>().ok("提交成功");
    }

    @PostMapping("/review")
    @RequiresPermissions("order:review")
    @Operation(summary = "审核员对单子进行审核")
    public Result<String> review(@RequestBody OrderReviewDTO orderReviewDTO) {
        orderService.review(orderReviewDTO);
        return new Result<String>().ok("审核成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    @LogOperation("删除")
    @RequiresPermissions("order:delete")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        orderService.delete(ids);
        return new Result();
    }
}
