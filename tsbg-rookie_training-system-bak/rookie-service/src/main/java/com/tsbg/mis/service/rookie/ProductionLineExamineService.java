package com.tsbg.mis.service.rookie;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.rookie.model.ExamineInternshipGrade;
import com.tsbg.mis.rookie.vo.*;
import com.tsbg.mis.util.ResultUtils;

import java.text.ParseException;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-22 15:02:00
 */
public interface ProductionLineExamineService {
    //考核评分日历
    ResultUtils inquireStudentscalendar();
    //考核评分列表
    ResultUtils inquireStudent();
    //考核评分列表 根据条件查询菁干班同学
    ResultUtils inquireStudentsByParams(InquireStudentsByParamsVo inquireStudentsByParamsVo);
    //产线实习 带出菁干班个人信息 实习内容及评语
    ResultUtils getStudentInformation(JSONObject json);
    //产线实习 个人潜力 内容提交
    ResultUtils insertExamineByLinneGroupLeader(ProductionLineInternshipVo productionLineInternshipVo);
    //查询菁干班同学出勤与奖惩内容
    ResultUtils inquireAttendaceRewardsAndPunishments(JSONObject json) throws ParseException;
    //修改菁干班同学出勤与奖惩内容
    ResultUtils updateAttendaceRewardsAndPunishments(AttendaceRewardsAndPunishmentsVo attendaceRewardsAndPunishmentsVo);
    //总评分 评分情况
    ResultUtils inquireRating();
    //评分情况 详细名单查看名单
    ResultUtils inquireRatingDetailedList(JSONObject json);
    //总评分提交
    ResultUtils insertTotalGrade(ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo);
    //产线负责人 个人潜力 内容提交
    ResultUtils insertExamineByLinneManager(ProductionLineInternshipVo productionLineInternshipVo);
    //查看当前菁干班同学产线实习月报
    ResultUtils getMonthlyReport(ReportProductionListVo reportProductionListVo);
     //查看当前菁干班同学产线实习周报
    ResultUtils getWeeklyReport(WeeklyReportProductionListVo weeklyReportProductionListVo);
    //查看总评分
    ResultUtils inquireTotalGrade(ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo);
}
