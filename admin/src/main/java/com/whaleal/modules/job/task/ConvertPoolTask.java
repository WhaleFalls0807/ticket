package com.whaleal.modules.job.task;

import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 17:57
 **/
@Slf4j
@Component("poolTask")
public class ConvertPoolTask implements ITask{

    private final OrderService orderService;

    private final ActivityService activityService;

    public ConvertPoolTask(OrderService orderService, ActivityService activityService) {
        this.orderService = orderService;
        this.activityService = activityService;
    }

    @Override
    public void run(String params) {
        log.info("开始执行超时单子转公海任务");
        orderService.pollUnlinkOrder();
        log.info("完成超时单子转公海任务");
    }
}
