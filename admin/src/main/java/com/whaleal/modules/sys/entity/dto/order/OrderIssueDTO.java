package com.whaleal.modules.sys.entity.dto.order;

import com.whaleal.modules.sys.entity.po.OrderTradeReceiptEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 15:12
 **/
@Data
@Schema(name = "管理员上传商标文件")
public class OrderIssueDTO {

    private Long orderId;

    private List<OrderTradeReceiptEntity> tradeList;
}
