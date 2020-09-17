package com.tsbg.mis.service.rookie;

import com.tsbg.mis.util.ResultUtils;

/**
 * @author 汪永晖
 */
public interface StudentTaskService {

    /**
     * 提交报告
     *
     * @param reportId
     * @param reportType
     * @return
     */
    ResultUtils submitReport(Integer reportId, Integer reportType, Integer lineId);

    /**
     * 提交提案改善报告
     *
     * @param proposalId
     * @return
     */
    ResultUtils improveReport(Integer proposalId, Integer lineId);

    /**
     * 提交部门报告
     *
     * @param reportId
     * @param reportType
     * @return
     */
    ResultUtils submitDepartReport(Integer reportId, Integer reportType);
}
