package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 14:37
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("order")
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 业务名称
     */
    private String name;

    /**
     *  业务类型
     *  前端借助数据字典实现 后端暂时以string 接收即可
     */
    private String  businessType;

    /**
     *  是否已成交
     */
    private Integer deal;

    /**
     * 支付类型 这里保存的是上传的支付截图 的地址链接
     */
    private String payType;

    /**
     *  总费用
     */
    private BigDecimal totalPrice;

    /**
     * 甲方承担价格
     */
    private BigDecimal aPrice;

    /**
     * 乙方承担价格
     */
    private BigDecimal bPrice;

    /**
     * 申请方式
     */
    private String applyMethod;

    /**
     * 上传的合同文件路径
     */
    private String contract;

    /**
     * 提交选项
     */
    private String commitOption;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 状态
     * 0：新建 1：已分配 2：已提交审核 3：审核中 4： 审核完成
     */
    private Integer status;

    /**
     * 审核人ID
     */
    private Long reviewUserId;

    /**
     *  关联客户id
     */
    private Long customerId;

    /**
     *  客户名称
     */
    private String customerName;

    /**
     * 负责人id
     */
    private long ownerId;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;

}
