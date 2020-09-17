package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.ExamineInternshipGrade;
import com.tsbg.mis.rookie.model.ProductionLineList;
import com.tsbg.mis.rookie.model.ReportProductionWeekQuestion;
import com.tsbg.mis.rookie.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-22 17:00:00
 */
public interface ProductionLineExamineDao {
    //考核评分(列表)
    List<ProductionLineExamineVo> inquireAll(String staffCode);
    //考核评分(日历)
    List<String> inquireStudentscalendarForName(String staffCode);


    //查询当前用户角色信息
    ArrayList<Integer> queryUserRole(String staffCode);

    //当前用户为产线组长时 旗下菁干班同学
    ArrayList<ExamineStudentVo> selectLeaderUnderStudents(@Param("lineId")  String lineId,@Param("internshipEnd") Date internshipEnd);

    //当前用户为产线负责人时 旗下菁干班同学
    ArrayList<ExamineStudentVo> selectManagerUnderStudents(String staffCode);

    //根据学生工号在考核评分记录表查找记录-->产线组长
    List<ExamineInternshipGrade> selectPendingApproval(@Param("creationId")Integer creationId, @Param("staffCode")String studentStaffCode);
    List<ExamineInternshipGrade> selectPendingApprovalForHR(@Param("creationId") Integer creationId, @Param("staffCode") String staffCode);


    //根据学生工号在考核评分记录表查找记录-->产线负责人
    List<ExamineInternshipGrade> selectPendingApprovalForManager(@Param("creationId")Integer creationId, @Param("staffCode")String studentStaffCode);

    //产线实习 -- 带出菁干班个人信息 实习内容及评语
    List<ProductionLineInternshipVo> getStudentInformation(@Param("staffCode") String staffCode,  @Param("studentStaffName") String studentStaffName);

    Integer insertExamineByLinneGroupLeader(ExamineInternshipGrade examineInternshipGrade);

    //根据工号查找最新lineId
    Integer selectLineIdByStaffCode(@Param("staffCode")String studentStaffCode);

    //查询菁干班同学出勤考核内容
    List<AttendanceVo> inquireAttendance(@Param("studentCode") String studentCode);

    //查询菁干班同学其他奖惩考核内容
    //List<OtherRewardsAndPunishmentsVo> inquireOtherRewardsAndPunishments(@Param("studentCode")String studentCode);

    //查询考核记录表内菁干班评分情况
    List<StudentsRatingVo> selectAllStudents(@Param("creationId")Integer creationId, @Param("examineTypeId") String examineTypeId);


    List<StudentInformationVo> queryStudentInformation(@Param("staffCode")String staffCode,@Param("studentStaffCode") String studentStaffCode,@Param("studentStaffName") String studentStaffName);

    InternshipCommentInformationVo queryInternshipCommentInformation(@Param("creationId")Integer creationId, @Param("staffCode")String staffCode,@Param("studentStaffCode") String studentStaffCode,@Param("studentStaffName") String studentStaffName);

    List<ExamineInternshipGrade> queryPersonalPotentialByLineGroupLeader(@Param("creationId")Integer creationId, @Param("staffCode") String staffCode, @Param("studentStaffCode") String studentStaffCode, @Param("studentStaffName") String studentStaffName);

    List<ExamineInternshipGrade> queryPersonalPotentialByLineManager(@Param("creationId")Integer creationId, @Param("staffCode") String staffCode, @Param("studentStaffCode") String studentStaffCode, @Param("studentStaffName") String studentStaffName);

    ArrayList<ExamineStudentVo> selectLeaderUnderStudentsByParam(@Param("lineId")  String lineId, @Param("studentStaffName")String studentStaffName,@Param("studentStaffCode")String studentStaffCode);

    ArrayList<ExamineStudentVo> selectNewStudentsByParam(@Param("studentStaffName")String studentStaffName,@Param("studentStaffCode")String studentStaffCode);

    ArrayList<ExamineStudentVo> selectManagerUnderStudentsByParam(@Param("staffCode") String staffCode, @Param("studentStaffName")String studentStaffName);
    //获取月报得分
    List<String> getMonthlyReportGrade(@Param("staffCode")String studentStaffCode);
    ///获取提案改善报告得分
    List<String> getProposalReportGrade(@Param("staffCode")String studentStaffCode);
    //获取当前菁干班同学所有产线实习月报
    List<ReportProductionListVo> getMonthlyReport(@Param("staffCode")String staffCode);
    //获取当前菁干班同学所有产线实习周报
    List<WeeklyReportProductionListVo> getWeeklyReport(@Param("staffCode") String staffCode, @Param("firstDate")Date firstDate,@Param("lastDate") Date lastDate);

    int insertAttendaceRewardsAndPunishments(ExamineInternshipGrade examineInternshipGrade);
    //获取周报下的问题与改善建议表数据
    List<ReportProductionWeekQuestion> getWeekQuestions(@Param("reportId") Integer reportId);
    //查询总评分(考核总得分)
    ExamineInternshipGrade inquireGrade(@Param("creationId")Integer creationId, @Param("staffCode") String staffCode, @Param("examineTypeId") String examineTypeId);
    //获得产线月度考核等级
    List<ExamineInternshipGrade> inquireRank(@Param("staffCode") String staffCode,@Param("creationId")Integer creationId);
    //获取产线实习总得分
    List<ExamineInternshipGrade> getProductionLineInternshipGrade(@Param("staffCode") String staffCode,@Param("creationId")Integer creationId);
    //找出组长旗下所有产线的数据库数据
    List<ProductionLineList> selectLeaderUnderLine(@Param("staffCode")String staffCode);
    //获取个人潜力总评分
    ExamineInternshipGrade inquirePersonalPotentialGrade(@Param("creationId")Integer creationId, @Param("staffCode") String staffCode, @Param("isReExamine") String isReExamine);
    //找出产线负责人旗下所有产线的数据库数据
    List<ProductionLineList> selectManagerUnderLine(@Param("staffCode")String staffCode);
    //根据ID获取typeName
    String getTypeNameById(@Param("typeId")String typeId);

    //获取考勤次数
    int getAttendaceNumber(@Param("studentStaffCode")String studentStaffCode,@Param("studentStaffName")  String studentStaffName,@Param("examineTypeId") int examineTypeId,@Param("internshipStart") Date internshipStart,@Param("internshipEnd") Date internshipEnd);
    //根据工号在考核记录表中查找考勤记录
    List<ExamineInternshipGrade> getAttendaceByParams(@Param("creationId")Integer creationId, @Param("studentStaffCode") String studentStaffCode);
    //根据工号在考核记录表中查找奖惩记录
    List<ExamineInternshipGrade> getRewardsAndPunishByParams(@Param("creationId") Integer creationId, @Param("studentStaffCode") String studentStaffCode);
    //获取奖惩次数
    int getRewardsAndPunishNumber(@Param("studentStaffCode")String studentStaffCode,@Param("studentStaffName")  String studentStaffName,@Param("examineTypeId") int examineTypeId,@Param("internshipStart") Date internshipStart,@Param("internshipEnd") Date internshipEnd);

    //查看同一个同学是否已经被评了实习内容和评语
    List<ExamineInternshipGrade> selectSameCommentDate(@Param("creationId") int creationId,@Param("studentStaffCode")String studentStaffCode);

    int updateSameCommentDate(ExamineInternshipGrade examineInternshipGrade);

    List<ExamineInternshipGrade> selectSameInternshipGradeDate(@Param("isReExamine")int isReExamine,@Param("creationId") int creationId,@Param("studentStaffCode")String studentStaffCode);

    int updateSameInternshipGradeDate(ExamineInternshipGrade examineInternshipGrade);
    int update(ExamineInternshipGrade examineInternshipGrade);

    List<ExamineInternshipGrade> selectSameExamineInternshipGradeDate(@Param("creationId")Integer creationId, @Param("studentStaffCode")String studentStaffCode);

    int updateSameExamineInternshipGradeDate(ExamineInternshipGrade examineInternshipGrade);

    List<ExamineInternshipGrade> selectSameAttendanceDate(@Param("creationId")Integer creationId,@Param("studentStaffCode") String studentCode);

    int updateSameAttendanceDate(ExamineInternshipGrade internshipGrade);
    //根据工号查姓名
    String getStaffNameByStaffCode(@Param("staffCode") String staffCode);
    //根据lineId查产线名
    String getLineNameById(@Param("lineId")Integer lineId);
    //查找最新的一届菁干班的同学
    ArrayList<ExamineStudentVo> selectNewStudents();
    //去考核记录表中获取记录
    List<String> getDataFromExamine(@Param("creationId")Integer creationId,@Param("studentStaffCode") String studentStaffCode,@Param("isReExamine") Integer isReExamine,@Param("examineTypeId") int examineTypeId);
    //查找最新一届菁干班同学对应的最新产线信息
    List<NewLineForStudentsVo> selectNewLineForStudents();
    //查找最新一届菁干班同学对应的最新产线信息(按条件查询)
    List<NewLineForStudentsVo> selectNewLineForStudentsByParam(@Param("staffName")String studentStaffName, @Param("staffCode")String studentStaffCode);
}
