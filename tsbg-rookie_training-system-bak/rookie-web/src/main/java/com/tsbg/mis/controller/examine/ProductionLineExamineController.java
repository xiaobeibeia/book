package com.tsbg.mis.controller.examine;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.model.ExamineInternshipGrade;
import com.tsbg.mis.rookie.model.ReportProductionWeekQuestion;
import com.tsbg.mis.rookie.vo.*;
import com.tsbg.mis.service.rookie.ProductionLineExamineService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-22 14:31:00
 */
@RestController
@RequestMapping("/examine/productionLine")
@Validated
@Api(value = "产线月度考核", tags = "产线月度考核")
public class ProductionLineExamineController {

    @Autowired
    ProductionLineExamineService productionLineExamineService;

    /**
     * 获取此工号下的所有菁干班同学产线月度考核列表
     * @return
     */
    @ApiOperation(value="查询此工号下菁干班同学产线月度考核列表", notes="查询此工号下菁干班同学产线月度考核列表", httpMethod = "GET")
    @RequestMapping(value="/inquireStudents", method= RequestMethod.GET)
    @UserLoginToken
    public ResultUtils inquireStudents(){
        return productionLineExamineService.inquireStudent();

    }



    @ApiOperation(value="根据条件查询此工号下菁干班同学产线月度考核列表", notes="根据条件查询此工号下菁干班同学产线月度考核列表", httpMethod = "POST")
    @ApiImplicitParam(name = "InquireStudentsByParamsVo对象",value = "传入参数为:\"examinStatus:考核状态(待评,已评); studentStaffName:考核同学姓名")
    @RequestMapping(value="/inquireStudentsByParams", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils inquireStudentsByParams(@RequestBody InquireStudentsByParamsVo inquireStudentsByParamsVo){
        return productionLineExamineService.inquireStudentsByParams(inquireStudentsByParamsVo);
    }


    /**
     * 查询考核评分日历
     * @return
     */
    @ApiOperation(value="查询此工号下菁干班同学产线月度考核评分(日历)", notes="查询此工号下菁干班同学产线月度考核评分(日历)", httpMethod = "GET")
    @RequestMapping(value="/inquireStudentscalendar", method= RequestMethod.GET)
    @UserLoginToken
    public ResultUtils inquireStudentscalendar(){
        return productionLineExamineService.inquireStudentscalendar();
    }


    /**
     * 产线月度考核--产线实习带出个人信息,实习内容及评语,品行信用,个人潜质等评分
     * @return
     */
    @ApiOperation(value="产线月度考核--产线实习带出个人信息,实习内容及评语,品行信用,个人潜质等评分", notes="产线月度考核--产线实习带出个人信息,实习内容及评语", httpMethod = "POST")
    @ApiImplicitParam(name = "json字符串",value = "传入参数为:\"studentStaffName:菁干班同学姓名,studentStaffCode:菁干班同学工号")
    @RequestMapping(value="/getStudentInformation", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils getStudentInformation(@RequestBody JSONObject json){
        return productionLineExamineService.getStudentInformation(json);
    }


    /**
     * 产线月度考核--产线实习 个人潜力内容提交
     * @param productionLineInternshipVo
     * @return
     */
    @ApiOperation(value="产线月度考核--产线组长 产线实习 个人潜力内容提交", notes="产线月度考核--产线实习 个人潜力内容提交", httpMethod = "POST")
    @ApiImplicitParam(name = "ProductionLineInternshipVo对象",value = "传入参数为:\"studentStaffCode:学生工号;lineGroupLeaderName:产线组长;" +
            "lineManagerName:产线负责人;internshipComment:实习内容;comment:评语;monthlyReportGrade:月报得分;proposalReportGrade:提案改善得分;" +
            "productionLineInternshipGrade:产线实习总得分;conductCreditByLineGroupLeader:品行信用得分;personalPotentialByLineGroupLeader:个人潜质得分" +
            "potentialGradeByLineGroupLeader:个人潜力初评总得分")
    @RequestMapping(value="/insertExamineByLinneGroupLeader", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils insertExamineByLinneGroupLeader(@RequestBody ProductionLineInternshipVo productionLineInternshipVo){
        return productionLineExamineService.insertExamineByLinneGroupLeader(productionLineInternshipVo);
    }

    /**
     * 产线月度考核--产线负责人 个人潜力内容提交
     * @param productionLineInternshipVo
     * @return
     */
    @ApiOperation(value="产线月度考核--产线负责人 个人潜力内容提交", notes="产线月度考核--产线负责人 个人潜力内容提交", httpMethod = "POST")
    @ApiImplicitParam(name = "ProductionLineInternshipVo对象",value = "传入参数为:\"studentStaffCode:学生工号;lineGroupLeaderName:产线组长;" +
            "lineManagerName:产线负责人;iconductCreditByLineManager:品行信用得分;personalPotentialByLineManager:个人潜质得分;" +
            "potentialGradeByLineManager:个人潜力复评总得分")
    @RequestMapping(value="/insertExamineByLinneManager", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils insertExamineByLinneManager(@RequestBody ProductionLineInternshipVo productionLineInternshipVo){
        return productionLineExamineService.insertExamineByLinneManager(productionLineInternshipVo);
    }


    /**
     * 查询菁干班同学出勤与奖惩
     * @param json
     * @return
     */
    @ApiOperation(value="查询菁干班同学出勤与奖惩", notes="查询菁干班同学考勤", httpMethod = "POST")
    @ApiImplicitParam(name = "json字符串",value = "传入参数为: studentStaffName:菁干班学生姓名;" +
            "studentStaffCode:菁干班学生工号", dataType = "JSONObject")
    @RequestMapping(value="/inquireAttendaceRewardsAndPunishments", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils inquireAttendaceRewardsAndPunishments(@RequestBody JSONObject json) throws ParseException {
        return productionLineExamineService.inquireAttendaceRewardsAndPunishments(json);
    }


    /**
     * 修改菁干班同学出勤与奖惩
     * @param attendaceRewardsAndPunishmentsVo
     * @return
     */
    @ApiOperation(value="修改菁干班同学出勤与奖惩", notes="修改菁干班同学出勤与奖惩", httpMethod = "PUT")
    @ApiImplicitParam(name = "AttendaceRewardsAndPunishmentsVo:出勤与奖惩对象",value = "传入参数为:AttendanceVo对象{examineType:考核类别;" +
            "countNum:次数;reason:原因;examineGrade:得分},OtherRewardsAndPunishmentsVo对象{examineType:考核类别;\" +\n" +
            "            \"countNum:次数;reason:原因;examineGrade:得分},attendanceAndRewardsAndPunishGrade:出勤与奖惩总分;studentStaffName:菁干班学生姓名;studentCode:菁干班学生工号")
    @RequestMapping(value="/updateAttendaceRewardsAndPunishments", method= RequestMethod.PUT)
    @UserLoginToken
    public ResultUtils updateAttendaceRewardsAndPunishments(@RequestBody AttendaceRewardsAndPunishmentsVo attendaceRewardsAndPunishmentsVo){
        return productionLineExamineService.updateAttendaceRewardsAndPunishments(attendaceRewardsAndPunishmentsVo);
    }


    /**
     * 查询评分情况
     * @return
     */
    @ApiOperation(value="查询评分情况", notes="查询评分情况", httpMethod = "GET")
    @RequestMapping(value="/inquireRating", method= RequestMethod.GET)
    @UserLoginToken
    public ResultUtils inquireRating(){
        return productionLineExamineService.inquireRating();
    }


    /**
     * 查看评分情况详细名单
     * @param json
     * @return
     */
    @ApiOperation(value="查看评分情况详细名单", notes="查看评分情况详细名单", httpMethod = "POST")
    @ApiImplicitParam(name = "json字符串",value = "传入参数为: examinedContent:考核内容(優 甲 乙 丙 丁)", dataType = "JSONObject")
    @RequestMapping(value="/inquireRatingDetailedList", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils inquireRatingDetailedList(@RequestBody JSONObject json){
        return productionLineExamineService.inquireRatingDetailedList(json);
    }



    /**
     * 提交总评分
     * @param productionLineExamineTotalGradeVo
     * @return
     */
    @ApiOperation(value="提交总评分", notes="提交总评分", httpMethod = "POST")
    @RequestMapping(value="/insertTotalGrade", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils insertTotalGrade(@RequestBody ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo){
        return productionLineExamineService.insertTotalGrade(productionLineExamineTotalGradeVo);
    }

    /**
     * 查询总评分
     * @param productionLineExamineTotalGradeVo
     * @return
     */
    @ApiOperation(value="查询产线实习月度考核总评分", notes="查询总评分", httpMethod = "POST")
    @ApiImplicitParam(name = "ExamineStudentVo对象",value = "传入参数为:\"staffName:菁干班同学姓名,staffCode:同学工号")
    @RequestMapping(value="/inquireTotalGrade", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils inquireTotalGrade(@RequestBody ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo){
        return productionLineExamineService.inquireTotalGrade(productionLineExamineTotalGradeVo);

    }


    //查看月报
    @ApiOperation(value="查看产线实习月报", notes="查看产线实习月报", httpMethod = "POST")
    @ApiImplicitParam(name = "ReportProductionListVo对象",value = "传入参数为:ReportProductionListVo对象" +
            "{staffCode:当前菁干班同学工号")
    @RequestMapping(value="/getMonthlyReport", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils getMonthlyReport(@RequestBody ReportProductionListVo reportProductionListVo){
        return productionLineExamineService.getMonthlyReport(reportProductionListVo);
    }
    //查看周报
    @ApiOperation(value="查看产线实习周报", notes="查看产线实习周报", httpMethod = "POST")
    @ApiImplicitParam(name = "WeeklyReportProductionListVo对象",value = "传入参数为:WeeklyReportProductionListVo对象对象" +
            "{staffCode:当前菁干班同学工号")
    @RequestMapping(value="/getWeeklyReport", method= RequestMethod.POST)
    @UserLoginToken
    public ResultUtils getWeeklyReport( @RequestBody WeeklyReportProductionListVo weeklyReportProductionListVo){
        return productionLineExamineService.getWeeklyReport(weeklyReportProductionListVo);
    }


}
