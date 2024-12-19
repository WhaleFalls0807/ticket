package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc 抢单配置实体类
 * @create: 2024-11-19 15:11
 **/
@Data
@TableName("user_grab_config")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGrabConfigEntity extends BaseEntity {

    /**
     * 粒度
     * 1：月
     * 2：周
     * 3：天
     * 4：时
     * 5：分
     * 6：秒
     */
    private Integer grain;

    /**
     * 抢单 刷新时间间隔
     * 此字段可能不需要使用
     */
    private Integer gap;

    /**
     * 某个粒度下 抢单的 总数量
     */
    private Long totalCount;

    /**
     *  两次抢单时间最少间隔 单位分钟
     */
    private Long grabGap;

    /**
     * 关联用户名 公共配置此字段为空
     */
    private String username;

    /**
     * 关联用户id 公共配置此字段为空
     */
    private Long userId;

}
