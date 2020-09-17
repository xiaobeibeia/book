package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.bag.MonthTargetApprovalInfoPackage;
import com.tsbg.mis.signed.bag.RookieApprovalInfoPackage;
import com.tsbg.mis.util.ResultUtils;

import java.util.List;

/**
 * @author 汪永晖
 */
public interface AuditTaskService {

    /**
     * 查询所有的审核任务
     *
     * @return
     */
    ResultUtils queryAll();

    /**
     * 查询所有报告类型
     *
     * @return
     */
    ResultUtils inquireBusiness();

    /**
     * 主管签核报告
     *
     * @param rookieApprovalInfoPackage
     * @return
     */
    ResultUtils auditReport(RookieApprovalInfoPackage rookieApprovalInfoPackage);


    /**
     * 人资签核部门主管的月目标
     *
     * @param monthTargetApprovalInfoPackage
     * @return
     */
    ResultUtils auditMonthTarget(MonthTargetApprovalInfoPackage monthTargetApprovalInfoPackage);

    /**
     * 查询同一批次的所有月目标
     *
     * @param targetNum
     * @return
     */
    ResultUtils querySameMonthTarget(String targetNum);

    /**
     * 检查一组 targetNum 是否是同一个月目标
     *
     * @param targetNumList
     * @return
     */
    ResultUtils checkSameMonthTarget(List<String> targetNumList);

    /**
     * 部门主管签核报告
     *
     * @param rookieApprovalInfoPackage
     * @return
     */
    ResultUtils departAudit(RookieApprovalInfoPackage rookieApprovalInfoPackage);
}
