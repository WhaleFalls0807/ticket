package com.whaleal.modules.sys.entity.vo;

import com.whaleal.modules.sys.entity.po.BusinessTypeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author lyz
 * @desc
 * @create: 2024-11-12 14:48
 **/
@Data
public class OrderVO {

    private Long id;

    private String orderName;

    private String content;

    private Long customerId;

    private String customerName;

    private String applyMethod;

    private String commitOption;

    private String payType;

    private String payment;

    private String contract;

    private String industry;

    private String phone;

    private String email;

    private Integer orderStatus;

    private Date createDate;

    private Long reviewUserId;

    private String reviewUsername;

    private Date reviewDate;

    private Long ownerId;

    private String ownerUsername;

    @Schema(name = "抢单后未付款天数")
    private Long daysUnPay;

    private Date dealDate;

    private BigDecimal aPrice;

    private BigDecimal bPrice;

    private BigDecimal totalPrice;

    private String  businessName;

    private List<BusinessTypeEntity> businessTypeList;
}
