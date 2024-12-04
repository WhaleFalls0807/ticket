package com.whaleal.modules.sys.controller;

import com.whaleal.common.annotation.LogOperation;
import com.whaleal.common.constant.Constant;
import com.whaleal.common.exception.OrderExceptionEnum;
import com.whaleal.common.page.PageData;
import com.whaleal.common.utils.Result;
import com.whaleal.common.validator.AssertUtils;
import com.whaleal.modules.security.user.SecurityUser;
import com.whaleal.modules.security.user.UserDetail;
import com.whaleal.modules.sys.entity.dto.order.*;
import com.whaleal.modules.sys.entity.po.CustomerEntity;
import com.whaleal.modules.sys.entity.po.OrderEntity;
import com.whaleal.modules.sys.entity.vo.OrderGrabVO;
import com.whaleal.modules.sys.entity.vo.OrderVO;
import com.whaleal.modules.sys.service.CustomerService;
import com.whaleal.modules.sys.service.OrderService;
import com.whaleal.modules.sys.service.UserGrabService;
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
import java.util.Objects;

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

    private final UserGrabService userGrabService;

    @GetMapping("/all/page")
    @Operation(summary = "分页获取列表")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, ref = "int"),
            @Parameter(name = "keyword", description = "关键字搜索", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "ownerId", description = "负责人id", in = ParameterIn.QUERY, ref = "long"),
            @Parameter(name = "orderStatus", description = "order状态", in = ParameterIn.QUERY, ref = "int"),
            @Parameter(name = "deal", description = "0：新建 1：待成单 2：已成单 3：公海", in = ParameterIn.QUERY, ref = "int",required = true),
            @Parameter(name = "sortField", description = "排序字段", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "isAsc", description = "是否升序", in = ParameterIn.QUERY, ref = "boolean"),
            @Parameter(name = "startDate", description = "开始时间", in = ParameterIn.QUERY, ref = "Date"),
            @Parameter(name = "endDate", description = "结束时间", in = ParameterIn.QUERY, ref = "Date"),
    })
    public Result<PageData<OrderVO>> pageAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        int deal = Integer.parseInt(params.get("deal").toString());
        if(deal == 3){
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
    @RequiresPermissions("approve:list")
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

    @Operation(summary = "查询订单详情")
    @GetMapping("/queryById/{id}")
    public Result<OrderVO> queryById(@PathVariable Long id){
        OrderVO OrderVO = orderService.findById(id);
        return new Result<OrderVO>().ok(OrderVO);
    }

    /**
     *  todo 加一些限流或其他安全机制 防止单设备攻击
     * @param orderDTO
     * @return
     */
    @PostMapping("/save/byUser")
    @Operation(summary = "普通用户通过门户网站新提一个商标注册申请")
    public Result<String> saveByUser(@RequestBody OrderDTO orderDTO) {
        if(orderService.saveSimpleOrder(orderDTO,false)){
            userGrabService.addGraped(1);
        }

        return new Result<String>().ok("创建成功");
    }

    /**
     *  todo 加一些限流或其他安全机制 防止单设备攻击
     * @param
     * @return
     */
    @PostMapping("/delete/file")
    @Operation(summary = "更新工单信息 - 专用于删除文件")
    public Result<String> updateOrder(@RequestBody OrderFileDeleteDTO orderFileDeleteDTO) {
        orderService.deleteFile(orderFileDeleteDTO);
        return new Result<String>().ok("创建成功");
    }

    @LogOperation("内部创建单子")
    @PostMapping("/save/inner")
    @Operation(summary = "业务员创建单子")
    @RequiresPermissions("sys:inner_order:save")
    public Result<String> saveInner(@RequestBody OrderDTO orderDTO) {
        if(orderService.saveSimpleOrder(orderDTO,true)){
            userGrabService.addRemain(1);
            userGrabService.grapeOrder(SecurityUser.getUserId(),1);
        }
        return new Result<String>().ok("创建成功");
    }

    @LogOperation("分配工单")
    @PostMapping("/distribute")
    @Operation(summary = "管理员分配单子")
    @RequiresPermissions("seas:assign")
    public Result<String> distributeOrder(@RequestBody OrderDistributeDTO orderDistributeDTO) {
        List<Long> orderIds = orderDistributeDTO.getOrderIds();
        if(orderIds.isEmpty()){
            return new Result<String>().ok("分配成功");
        }
        if(ObjectUtils.isEmpty(orderDistributeDTO.getUserId())){
            orderDistributeDTO.setUserId(SecurityUser.getUserId());
        }
        long l = orderService.distributeOrder(orderDistributeDTO.getOrderIds(), orderDistributeDTO.getUserId());

        // 分配的单子不占用业务员抢单子上限
        if(l != 0){
            userGrabService.addRemain(-1);
            userGrabService.addGraped(1);
//            userGrabService.grapeOrder(orderDistributeDTO.getUserId(),l);
        }
        return new Result<String>().ok("分配成功");
    }

    @LogOperation("领取单子")
    @PostMapping("/choose")
    @Operation(summary = "业务员领取单子")
    @RequiresPermissions("grab:grab")
    public Result chooseOrder(@RequestBody OrderDistributeDTO orderDistributeDTO) {
        Long userId = SecurityUser.getUserId();
        orderDistributeDTO.setUserId(userId);
        long l = orderService.distributeOrder(orderDistributeDTO.getOrderIds(), userId);

        if(l != 0){
            userGrabService.grapeOrder(orderDistributeDTO.getUserId(),l);
        }
        return new Result();
    }

    @LogOperation("抢单")
    @GetMapping("/grab")
    @Operation(summary = "业务员抢单子-系统随机分配一个单子")
    @RequiresPermissions("grab:grab")
    public Result grabOrder() {
        Long userId = SecurityUser.getUserId();

        //已抢单数量小于1 不能再进行抢单
        OrderGrabVO countByUserId = userGrabService.findCountByUserId(userId);
        if(countByUserId.getUserRemainCount() < 1){
            return new Result().ok("此时间周期内已不允许抢单");
        }

        OrderEntity orderEntity = orderService.electOrder(userId);
        if(ObjectUtils.isEmpty(orderEntity)){
            return new Result().ok("没有可以抢的单子");
        }

        userGrabService.grapeOrder(userId,1);
        return new Result().ok(orderEntity);
    }

    @LogOperation("补充单子信息")
    @PostMapping("/info/add")
    @Operation(summary = "补充单子信息")
    @RequiresPermissions("todo:update")
    public Result addInformation(@RequestBody OrderUpdateDTO orderUpdateDTO) {
        OrderEntity orderEntity = orderService.selectById(orderUpdateDTO.getId());
        if(null == orderEntity){
            return new Result().error(OrderExceptionEnum.ORDER_NOT_EXISTS.getCode(),OrderExceptionEnum.ORDER_NOT_EXISTS.getMsg());
        }

        Long ownerId = orderEntity.getOwnerId();
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() != 1){
            if(!Objects.equals(user.getId(), ownerId)){
                return new Result().error(OrderExceptionEnum.NO_PERMISSION_OPERATE.getCode(),OrderExceptionEnum.NO_PERMISSION_OPERATE.getMsg());
            }
        }

        if(!ObjectUtils.isEmpty(orderUpdateDTO.getCustomerId())){
            CustomerEntity customerEntity = customerService.selectById(orderUpdateDTO.getCustomerId());
            if(!ObjectUtils.isEmpty(customerEntity)){
                orderUpdateDTO.setCustomerName(customerEntity.getCustomerName());
                orderUpdateDTO.setPhone(customerEntity.getPhone());
                orderUpdateDTO.setEmail(customerEntity.getEmail());
                orderUpdateDTO.setIndustry(customerEntity.getIndustry());
            }
        }
        orderService.addInformation(orderEntity,orderUpdateDTO);
        return new Result();
    }

    @LogOperation("提交工单")
    @PostMapping("/commit")
    @Operation(summary = "业务员提交单子")
    @RequiresPermissions("todo:commit")
    public Result<String> commitForReview(@RequestBody OrderCommitDTO orderCommitDTO ) {
        orderService.commit(orderCommitDTO);
        return new Result<String>().ok("提交成功");
    }

    @LogOperation("修改工单状态")
    @PostMapping("/status/change")
    @Operation(summary = "修改工单状态",description = "1: 放回公海  2: 指派给其他人 3: 成单")
    public Result<String> editOrderStatus(@RequestBody OrderEditDTO orderEditDTO ) {
        orderService.editStatus(orderEditDTO);
        return new Result<String>().ok("提交成功");
    }

    @PostMapping("/review")
    @RequiresPermissions("approve:approve")
    @Operation(summary = "审核员对单子进行审核")
    @LogOperation("审核工单")
    public Result<String> review(@RequestBody OrderReviewDTO orderReviewDTO) {
        orderService.review(orderReviewDTO);
        return new Result<String>().ok("审核成功");
    }

//    @LogOperation("提交商标正式文件")
//    @PostMapping("/issue/order/brand")
//    @Operation(summary = "管理员提交商标正式文件")
//    @RequiresPermissions("order:issue")
//    public Result<String> issueOrderBrand(@RequestBody OrderIssueDTO orderIssueDTO) {
//        orderTradeService.issueOrderBrade(orderIssueDTO);
//        return new Result<String>().ok("创建成功");
//    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    @LogOperation("删除工单")
    @RequiresPermissions("todo:delete")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        orderService.delete(ids);

        // 重新初始化一下池子信息
        userGrabService.initPoolCount();
        // todo 清空重置单子信息
        return new Result();
    }
}
