package com.whaleal.modules.sys.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-11 22:33
 **/
@Data
public class OrderPriceVO {

    /**
     * 官费
     */
    private BigDecimal officialPrice;

    /**
     * 代理费
     */
    private BigDecimal agencyPrice;

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

}
