package com.whaleal.modules.sys.enums;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-05 22:33
 **/
public interface OrderConstant {

    /**
     * 单子状态
     *  0：新建(待分配)
     *  1：已分配(确认客户信息中)
     *  2: 已确认(编辑order资料中)
     *  3：已提交(待审核)
     *  4：已审核(待二次提交)
     *  5：二次提交(待二次审核)
     *  6: 已完成
     *
     *  7：异常
     *  8：已删除
     */
    int CREATED = 0;
    int DISTRIBUTED = 1;
    int CONFIRMED = 2;
    int COMMITTED = 3;
    int REVIEWED = 4;
    int SUBMIT_AGAIN = 5;
    int COMPLETED = 6;
    int EXCEPTION = 7;
    int DELETED = 8;



}
