package com.whaleal.modules.sys.service;

import com.whaleal.modules.sys.entity.dto.order.OrderIssueDTO;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 22:35
 **/
public interface OrderTradeService {

    /**
     * 上传回执证书
     * @param orderIssueDTO
     */
    void issueOrderBrade(OrderIssueDTO orderIssueDTO);

}
