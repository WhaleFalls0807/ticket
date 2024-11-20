package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lyz
 * @desc 企业文书下载实体
 * @create: 2024-11-19 10:48
 **/
@Data
@TableName("download_record")
@EqualsAndHashCode(callSuper=false)
public class DownloadRecord implements Serializable {

    @TableId
    private Long id;

    /**
     * 关联业务id
     */
    private Long associationId;

    /**
     * 下载者用户名
     */
    private String username;

    /**
     * 下载者用户id
     */
    private Long userId;

    /**
     * 自动生成的，合同编号
     * 企业文书处用到
     */
    private String contractNum;

    /**
     * 下载状态 成功还是失败
     * 0：成功
     * 1：失败
     */
    private Integer success;

    /**
     * 失败原因
     */
    private String failedReason;

    /**
     * 下载时间
     */
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    private Date downloadDate;
}
