package com.whaleal.modules.sys.entity.dto;

import com.whaleal.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2024-12-03 15:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    /**
     * 关联id
     */
    private Long associationId;


    private List<Long> receiveIds;

    private String content;

    /**
     * 1：分配单子
     * 2：提交单子
     * 3：审核了单子
     * 4：单子拥有权转移
     * 5: 成单后管理员上传商标文件
     *
     * 11：下载了企业文书
     *
     *
     */
    private Integer type;


}
