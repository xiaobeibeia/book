package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.bag.MonthlyTargetPackage;
import com.tsbg.mis.util.ResultUtils;

import java.util.List;

/**
 * @author 汪永晖
 */
public interface WeekTargetService {

    /**
     * 查询月目标所有审核人
     *
     * @return
     */
    ResultUtils queryReviewer();

    /**
     * 创建完月目标，提交到人资去审核
     *
     * @return
     */
    ResultUtils reviewMonthlyTarget(MonthlyTargetPackage monthlyTargetPackage);

    /**
     * 查询所有的月目标审核任务
     *
     * @return
     */
    ResultUtils queryMonthTarget();

    /**
     * 查询个人所有的目标（周和月）
     *
     * @return
     */
    ResultUtils queryAllTarget();
}
