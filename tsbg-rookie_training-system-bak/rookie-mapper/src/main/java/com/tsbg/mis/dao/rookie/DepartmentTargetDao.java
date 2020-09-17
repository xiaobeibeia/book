package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.jurisdiction.bag.RoleAndInfoPackage;
import com.tsbg.mis.master.StaffInfo;
import com.tsbg.mis.rookie.bag.WeekDepartTargetPackage;
import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.rookie.vo.DepartTargetSetInfoList;
import com.tsbg.mis.rookie.vo.TargetVo;
import com.tsbg.mis.rookie.vo.WeekDepartTargetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentTargetDao {
    //查询所有类型
    List<TypeList> queryAllType();

    //根据工号查询用户角色信息
    List<RoleAndInfoPackage> queryUserRole(String staffCode);

    //根据工号查询员工信息
    StaffInfo queryStaffInfo(String staffCode);

    //查询所有届别
    List<ReportCreationList> queryClassPeriod();

    //根据届别查询当前届别下的所有报告任务
    List<ReportCreationList> queryReportCreation(String classPeriod);

    //根据届别查询当前设立人所在部门下的所有菁干班成员信息
    List<ClassStudentInfo> queryClassStudentInfo(ClassStudentInfo classStudentInfo);

    //新增目标信息表
    Integer insertDepartTargetLists(DepartTargetList departTargetLists);

    //新增目标学生信息表
    Integer insertDepartTargetStudent(DepartTargetStudent departTargetStudent);

    //查询目标单号
    String queryTargetNum();

    //根据目标单号查询目标详情
    List<DepartTargetList> queryTargetDetailsInfo(List<String> targetNumList);

    //根据目标单号查询目标详情
    List<DepartTargetStudent> queryTargetStudentInfo(List<String> targetNumList);

    //部门月、周目标编辑
    Integer updateTargetInfo(DepartTargetList departTargetList);

    //查询月报、周报
    List<ReportCreationList> queryMonthlyWeekReportInfo(ReportCreationList reportCreationList);

    /**
     * 修改月目标的状态
     *
     * @param targetNum
     * @return
     */
    int updateTargetInfosByTargetNum(String targetNum);

/*    //根据当前登录人的处级单位名称去查询当前登录人处级单位名称下面的所有学生信息
    List<ClassStudentInfo> selectStudentInfo(String BUName);*/

    //根據單號刪除目標信息
    Integer deleteTargetInfo(String targetNum);

    //根據單號刪除學生
    Integer deleteStudentTarget(String targetNum);

    //查询月报告任务
    List<DepartTargetSetInfoList> selectMonthReportCreationList(DepartTargetSetInfoList DepartTargetSetInfoList);

    //根据学生工号查询目标信息
    List<DepartTargetSetInfoList> queryTargetInfo(DepartTargetSetInfoList departTargetSetInfoList);

    DepartTargetSetInfoList queryApprovalInfo(DepartTargetSetInfoList departTargetSetInfoList);

    //根据creationId查询对应报告任务的开始时间和结束时间
    DepartTargetList queryReportCreationList(Integer creationId);

    Integer deleteApprovalInfo(@Param("investTableName") String investTableName,
                               @Param("investFieldName") String investFieldName,
                               @Param("investFieldValue") String investFieldValue);

    //查询是否已创建周目标信息
    List<DepartTargetList> queryCreateTargetInfo(DepartTargetList departTargetList);

    //根据creationId查询月报时间
    DepartTargetList queryReportCreationInfo(WeekDepartTargetPackage weekDepartTargetPackage);

    //根据月报时间查询周报
    List<WeekDepartTargetVo> queryWeekReportInfo(DepartTargetList departTargetList);

    //根据周报时间查询是否有设立目标信息
    List<DepartTargetList> queryWeekTargetInfo(DepartTargetList departTargetList);

    /**
     * 根据targetNum 和周报的creationId 查询周目标
     *
     * @return
     */
    List<DepartTargetList> queryWeekTargetExist(@Param("monthCreationId") Integer monthCreationId, @Param("weekCreationId") Integer weekCreationId, @Param("list") List<String> targetNumList);

    /**
     * 修改月目标为不可修改
     *
     * @param monthTargetNum
     * @return
     */
    int updateMonthTargetState(String monthTargetNum);

    /**
     * 查询所有目标
     *
     * @param staffCode
     * @return
     */
    List<TargetVo> queryAllTarget(String staffCode);

    DepartTargetList selectCreateDateByTargetNum(String targetNum);
}
