package com.whaleal.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc 企业文书
 * @create: 2024-11-19 10:10
 **/
@TableName("corporate_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorDocumentsEntity extends BaseEntity {

    /**
     * 文书名称
     */
    private String fileName;

    /**
     * 文书路径
     */
    private String filePath;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 文书类型 数据字典控制
     */
    private String type;

    /**
     * 下载数
     */
    @TableField(exist = false)
    private Long count;

    /**
     * 创建者用户名
     */
    @TableField(exist = false)
    private String createName;

    /**
     * 更新者
     */
    private String updater;
}
