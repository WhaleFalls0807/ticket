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
     *  1：已分配(待补充单子与客户信息)
     *  2: 提交待审核
     *  3  审核通过 待二次提交
     *  4: 审核被驳回 待重新提交
     *  5: 二次提交待审核
     *  6: 二次审核被驳回 待重新提交
     *  7：审核通过
     *  8: 已成单
     *  9：公海中的单子
     *  10: 完单（单子已完成）
     *  11：异常
     *
     *  99：已删除
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

    int FINISH = 10;

    int EXCEPTION = 11;

    int DELETED = 99;




    // 系统用户
    long SYSTEM_ID = 888;
    // 匿名用户
    long ANNO_USER_ID = 999;

    String SYSTEM_NAME = "System";

    String ANNO_USER_NAME = "Anonymous";


}
