package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.bag.DepartTargetSetInfoPackage;
import com.tsbg.mis.rookie.bag.WeekDepartTargetPackage;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.model.DepartTargetList;
import com.tsbg.mis.rookie.model.ReportCreationList;
import com.tsbg.mis.util.ResultUtils;


public interface DepartmentTargetService {

    //查詢type_list表中所有數據
    ResultUtils queryAllType();

    //根据工号查询员工信息
    ResultUtils queryStaffInfo();

    //查询所有届别
    ResultUtils queryClassPeriod();

    //根据届别查询当前届别下的所有报告任务
    ResultUtils queryReportCreation(String classPeriod);

    //根据届别查询当前设立人所在部门下的所有菁干班成员信息
    ResultUtils queryClassStudentInfo(ClassStudentInfo classStudentInfo);

    //设立部门月、周目标
    ResultUtils insertDepartmentTargetInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage);

    //根据目标单号查询目标详情
    ResultUtils queryTargetDetailsInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage);

    //部门月、周目标编辑
    //ResultUtils updateTargetInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage);

    //部门月目标查询
    ResultUtils queryDepartmentMonthTargetInfo();

    //部门周目标查询
    ResultUtils queryDepartmentWeekTargetInfo();

    //查询月报、周报
    ResultUtils queryMonthlyWeekReportInfo(ReportCreationList reportCreationList);

    //批量編輯月目標
    ResultUtils updateMonthlyTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception;

    //查询是否已创建周目标信息
    ResultUtils queryCreateTargetInfo(WeekDepartTargetPackage weekDepartTargetPackage);

    /**
     * 单个创建周目标
     *
     * @param departTargetSetInfoPackage
     * @return
     */
    ResultUtils createWeekTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage);

    /**
     * 修改周目标
     *
     * @param departTargetSetInfoPackage
     * @return
     */
    ResultUtils updateWeekTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception;
}
