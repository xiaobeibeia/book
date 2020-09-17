package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.util.ResultUtils;

import java.util.Date;
import java.util.List;

public interface ReportCreationListService {

    ResultUtils creationReport(ReportCreationList reportCreationList) throws Exception;

    ResultUtils selectStateListByTab(Integer tab);

    ResultUtils selectTypeListByTab(Integer tab,Integer pid);

    ResultUtils insertLineReport(ReportProductionList reportProductionList) throws Exception;

    ResultUtils insertDepartReport(ReportDepartList reportDepartList) throws Exception;//填写部门报告

    ResultUtils insertProposalReport(ReportProductionProposalList reportProductionProposalList) throws Exception;

    ResultUtils fuzzySearchReport(ReportCreationList reportCreationList);

    ResultUtils updateReport(ReportCreationList reportCreationList);

    ResultUtils selectAllReportByStaffCode();

    ResultUtils selectWhichLine(String staffCode, Date missionEndTime);

    ResultUtils selectReportInfo(Integer reportId,Integer missionType);

    ResultUtils updateReportByReportId(ReportProductionList reportProductionList) throws Exception;

    ResultUtils selectWeekApprovalInfo(Integer missionType,Integer reportId,Integer creationId);//查詢月報下所有週報的評分評語

    ResultUtils deleteMember(String staffCode);

    ResultUtils updateProposalReport(ReportProductionProposalList reportProductionProposalList) throws Exception;

    ResultUtils selectTargetInfo(ReportDepartList reportDepartList);//填寫部門!週報!時查找目標

    ResultUtils selectDailyByWeek(Integer creationId);//查询周报下的日报内容

    ResultUtils selectMenuByRole();

    ResultUtils selectClassPeriodList();

    ResultUtils insertAttendance(List<AttendanceRecord> attendanceRecords);

    ResultUtils importRewardsAndPunishment(List<RewardPunishRecord> rewardPunishRecords);

    ResultUtils selectMemberByPage(Integer pageNum);
    ResultUtils queryRewardPunishRecord(Integer PageNum);

    ResultUtils queryAttendanceRecord(Integer pageNum);

    ResultUtils updateDepartReport(ReportDepartList reportDepartList) throws Exception;

    //修改部門實習報告
//    ResultUtils updateDepartReportInfo(ReportDepartList reportDepartList) throws Exception;

}
