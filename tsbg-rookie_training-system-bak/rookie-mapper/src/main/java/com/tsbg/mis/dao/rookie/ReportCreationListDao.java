package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.rookie.vo.DepartTargetListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
@Component
public interface ReportCreationListDao {
    Integer creationReport(ReportCreationList reportCreationList);//创建报告

    List<StateList> selectStateListByTab(Integer tab);

    List<TypeList> selectTypeListByTab(@Param("tab") Integer tab,
                                       @Param("pid") Integer pid);


    Integer insertLineReport(ReportProductionList reportProductionList);//填写产线报告

    Integer insertDepartReport(ReportDepartList reportDepartList);//填写部门报告

    Integer insertProposalReport(ReportProductionProposalList reportProductionProposalList);//填写提案改善类报告(包括在产线报告里)


    List<ReportCreationList> fuzzySearchReport(ReportCreationList reportCreationList);//模糊搜索任务报告

    Integer updateReport(ReportCreationList reportCreationList);

    List<ReportCreationList> selectReportNotFillIn(String staffCode);//查找当前学生所有未填写的报告

    List<ReportCreationList> selectReportFillInProductionList(String staffCode);

    List<ReportCreationList> selectReportFillInDepart(String staffCode);

    List<ReportCreationList> selectReportFillInProposal(String staffCode);

    Integer updateCreationIsEnableWhereOverdue(Date now);

    ProductionLineList selectWhichLine(@Param("staffCode")String staffCode,
                                       @Param("missionEndTime") Date missionEndTime);

    ReportProductionList selectProductionReportInfo(Integer reportId);

    Integer updateReportByReportId(ReportProductionList reportProductionList);

    Integer insertProductionQuestion(ReportProductionWeekQuestion reportProductionWeekQuestion);

    List<RookieApprovalInfo> selectApprovalInfo(@Param("investTableName") String investTableName,
                                                @Param("investFieldValue")Integer investFieldValue,
                                                @Param("reportType")Integer reportType);

    ReportDepartList selectDepartReportInfo(Integer reportId);

    ReportProductionProposalList selectProposalReportInfo(Integer reportId);

    List<ReportProductionWeekQuestion> selectProductionQuestion(Integer reportId);

    Integer insertDepartWork(ReportDepartWork reportDepartWork);

    List<RookieApprovalInfo> selectWeekApprovalInfo(@Param("reportType") Integer reportType,
                                                    @Param("creationId") Integer creationId,
                                                    @Param("staffCode") String staffCode,
                                                    @Param("reportId") Integer reportId);

    //删除相关---↓
    Integer deleteLineReport(String staffCode);//删除产线实习报告

    Integer deleteWeekQuestionByReportId(Integer reportId);//删除产线问题建议表

    Integer deleteProposalReport(String staffCode);//删除提案改善报告

    Integer deleteDepartReport(String staffCode);//删除部门报告表

    Integer deleteDepartWork(Integer reportId);//删除部门工作信息表

    Integer deleteReportDepartTarget(Integer reportId);//删除部门目标中间表

    Integer deleteStaffRole(String staffCode);//刪除角色(順便也會不能登錄


    List<Integer> selectProductionReportIdByStaffCode(String staffCode);

    List<Integer> selectDepartReportIdByStaffCode(String staffCode);

    Date selectStartTimeInLine();

    Date selectEndTimeInLine();

    Integer updateProposalReport(ReportProductionProposalList reportProductionProposalList);

    List<DepartTargetListVo> selectTargetInfo(ReportCreationList reportCreationList);

    List<DepartTargetList> selectTargetResult(Integer targetId);

    Integer insertReportDepartTarget(ReportDepartTarget reportDepartTarget);

    Integer insertFileInfo(FileInfo fileInfo);

    FileInfo selectFileInfo(FileInfo fileInfo);

    ReportCreationList selectTimeInCreationList(Integer creationId);

    Integer updateFileInfo(FileInfo fileInfo);

    Integer deleteAllFile(FileInfo fileInfo);

    //修改部門實習報告
    Integer updateReportDepartList(ReportDepartList reportDepartList);

/*    Integer deleteReportDepartWork(Integer reportId);

    Integer updateReportDepartTarget(ReportDepartTarget reportDepartTarget);

    Integer addReportDepartWork(@Param("reportDepartWorkList") List<ReportDepartWork> reportDepartWorkList);

    Integer updateDepartTargetList(DepartTargetList departTargetList);*/

//    Integer updateDepartTargetResultList(DepartTargetResultList departTargetResultList);

    List<ReportDepartWork> selectDepartWork(Integer reportId);

    List<ReportDepartTarget> selectDepartTarget(Integer reportId);

    //查找產線週報下的產線日報內容
    List<ReportProductionList> selectDailyByWeek(ReportProductionList reportProductionList);


    //查找启用的任务
    List<ReportCreationList> selectEnableReport();
    //对文件进行软删除
    Integer deleteFileInfo(FileInfo fileInfo);

    List<ClassGradeInfo> selectClassPeriodList();

    List<DepartTargetList> selectTarget(ReportDepartList reportDepartList);

    Integer insertAttendance(AttendanceRecord attendanceRecord);

    Integer importRewardsAndPunishment(RewardPunishRecord rewardPunishRecord);

    List<ClassStudentInfo> selectMenberInfoList();

    //查询奖惩记录
    List<RewardPunishRecord> queryRewardPunishRecord();

    //查询考勤记录
    List<AttendanceRecord> queryAttendanceRecord();

    List<ReportDepartWork> selectLeaveOverMission(ReportDepartList reportDepartList);

    List<ReportDepartWork> selectNextOtherMission(@Param("tab")Integer tab,
                                                  @Param("missionStartTime")Date missionStartTime,
                                                  @Param("reportType")Integer reportType,
                                                  @Param("staffCode")String staffCode);

    List<DepartTargetList> selectNextTarget(ReportDepartList reportDepartList);//查找下週目標

    Integer updateReportDepartWork(ReportDepartWork reportDepartWork);

    ReportDepartWork selectWorkPid(Integer workId);

    Integer updateWorkIsContinue(Integer workId,Integer isContinue);

    Integer selectStudentIsExist(String staffCode);
}
