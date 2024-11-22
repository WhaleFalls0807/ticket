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
     *  2: 提交待审核
     *  3  审核通过 待二次提交
     *  4: 审核被驳回 待重新提交
     *  5: 二次提交待审核
     *  6: 二次审核被驳回 待重新提交
     *  7：审核通过
     *  8: 已成单
     *
     *  8：公海中的单子
     *  9：已删除
     */
    int CREATED = 0;
    int DISTRIBUTED = 1;
    int WAIT_REVIEW = 2;

    int WAIT_COMMIT_TWICE = 3;
    int REVIEW_REJECT = 4;

    int WAIT_REVIEW_TWICE = 5;
    int TWICE_REVIEW_REJECT = 6;

    int REVIEW_COMPLETE = 7;

    int SUCCESS = 8;
    int IN_POOL = 9;

    int EXCEPTION = 10;
    int DELETED = 99;


    /**
     *     activity 常量
     */
    int SYSTEM_GENERATE = 1;
//    int SYSTEM_GENERATE = 1;
//    int SYSTEM_GENERATE = 1;
//    int SYSTEM_GENERATE = 1;
//    int SYSTEM_GENERATE = 1;

}
