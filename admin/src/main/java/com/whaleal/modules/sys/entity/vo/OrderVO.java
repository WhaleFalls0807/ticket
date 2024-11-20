package com.whaleal.modules.sys.entity.vo;

import com.whaleal.common.utils.ConvertUtils;
import com.whaleal.modules.sys.entity.po.OrderFileEntity;
import com.whaleal.modules.sys.entity.po.OrderPriceEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 14:48
 **/
@Data
public class OrderVO {

    private Long id;

    private String orderName;

    private Long customerId;

    private String customerName;

    private String industry;

    private String phone;

    private Integer orderStatus;

    private Date createDate;

    private OrderFileVO orderFileVO;

    private OrderPriceVO orderPriceVO;

    public void setOrderFileVO(OrderFileEntity orderFileEntity) {
        this.orderFileVO = ConvertUtils.sourceToTarget(orderFileEntity, OrderFileVO.class);
    }

    public void setOrderPriceVO(OrderPriceEntity orderPriceEntity) {
        this.orderPriceVO = ConvertUtils.sourceToTarget(orderPriceEntity, OrderPriceVO.class);
    }
}
