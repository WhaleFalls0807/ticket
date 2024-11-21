package com.whaleal.modules.sys.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-20 20:49
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFileVO {

    private String contract;

//    private String contractName;

    private String payType;

//    private String payTypeName;

    private String logo;

//    private String logoName;

    private String IDCard;

//    private String IDCardName;

    private String applyBook;

//    private String applyBookName;

    private String commission;

//    private String commissionName;

    private String businessLicense;

//    private String businessLicenseName;

    private String sealedContract;

//    private String sealedContractName;
}

