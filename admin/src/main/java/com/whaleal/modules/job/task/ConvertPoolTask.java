package com.whaleal.modules.job.task;

import com.whaleal.modules.sys.entity.dto.ActivityDTO;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-29 17:57
 **/
@Slf4j
@Component
public class ConvertPoolTask{

    private final OrderService orderService;


    public ConvertPoolTask(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Shanghai")
    public void run() {
        log.info("开始执行超时单子转公海任务");
        orderService.pollUnlinkOrder();
        log.info("完成超时单子转公海任务");
    }
}
