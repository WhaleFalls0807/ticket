package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc 业务文件实体类
 * @create: 2024-11-11 21:31
 **/
@Data
@TableName("order_file")
@AllArgsConstructor
@NoArgsConstructor
public class OrderFile extends BaseEntity {

    /**
     * 上传的合同文件路径 -- 第一次上传
     */
    private String contract;

    /**
     * 支付类型 这里保存的是上传的支付截图 的地址链接 -- 第一次上传
     */
    private String payType;

    /**
     * logo
     */
    private String logo;

    /**
     * 身份证
     */
    private String IDCard;

    /**
     * 申请书
     */
    private String applyBook;

    /**
     * 委托书
     */
    private String commission;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 盖章合同
     */
    private String sealedContract;


}
