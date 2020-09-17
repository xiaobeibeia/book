package com.tsbg.mis.controller.rookie;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.service.rookie.ProductionLineListService;
import com.tsbg.mis.service.rookie.ReportCreationListService;
import com.tsbg.mis.util.ResultUtils;
import com.tsbg.mis.util.excel.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reportCreationList")
@Api(tags = "菁干班系统-实习报告管理",value = "菁干班系统-实习报告管理")
public class ReportCreationListController {

    @Autowired
    private ReportCreationListService reportCreationListService;
    @Autowired
    private ProductionLineListService productionLineListService;


    @UserLoginToken
    @RequestMapping(value = "/creationReport",method = RequestMethod.POST)
    @ApiOperation(value = "创建任务报告")
    @ApiImplicitParam(name = "reportCreationList:报告对象",value = "传入参数为：\"missionType：任务类型;internshipStart:实习开始时间;internshipEnd:实习结束时间;defaultStartTime:默认开始时间;defaultEndTime:默认结束时间")
    //创建任务报告
    public ResultUtils creationReport (@RequestBody ReportCreationList reportCreationList) throws Exception{
        return reportCreationListService.creationReport(reportCreationList);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectStateListByTab",method = RequestMethod.GET)
    @ApiOperation(value = "根据tab查找state列表")
    //根据tab查找state列表
    public ResultUtils selectStateListByTab(Integer tab){
        return reportCreationListService.selectStateListByTab(tab);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectTypeListByTab",method = RequestMethod.GET)
    @ApiOperation(value = "根据tab查找Type列表")
    //根据tab查找state列表
    public ResultUtils selectTypeListByTab(Integer tab,Integer pid){
        return reportCreationListService.selectTypeListByTab(tab,pid);
    }

    @UserLoginToken
    @RequestMapping(value = "/fuzzySearchReport",method = RequestMethod.POST)
    @ApiOperation(value = "模糊搜索报告任务")
    @ApiImplicitParam(name = "reportCreationList",value = "传入参数为:\"missionType:任务类型;reportType:报告类型;status:启用状态;missionStartTime:报告对应开始时间;missionEndTime:报告对应结束时间")
    //模糊搜索报告任务
    public ResultUtils fuzzySearchReport(@RequestBody ReportCreationList reportCreationList){
        return reportCreationListService.fuzzySearchReport(reportCreationList);
    }

    @UserLoginToken
    @RequestMapping(value = "/insertLineReport",method = RequestMethod.POST)
    @ApiOperation(value = "填写产线实习报告")
    //填写报告
    public ResultUtils insertLineReport(@RequestBody ReportProductionList reportProductionList) throws Exception{
        return reportCreationListService.insertLineReport(reportProductionList);
    }

    //填写部门报告
    @UserLoginToken
    @RequestMapping(value = "/insertDepartReport",method = RequestMethod.POST)
    @ApiOperation(value = "填写部门实习报告")
    public ResultUtils insertDepartReport(@RequestBody ReportDepartList reportDepartList) throws Exception {
        return reportCreationListService.insertDepartReport(reportDepartList);
    }

    @UserLoginToken
    @RequestMapping(value = "/insertProposalReport",method = RequestMethod.POST)
    @ApiOperation(value = "填写提案改善类报告")
    public ResultUtils insertProposalReport(@RequestBody ReportProductionProposalList reportProductionProposalList) throws Exception{
        return reportCreationListService.insertProposalReport(reportProductionProposalList);
    }

    @UserLoginToken
    @RequestMapping(value = "/updateReport",method = RequestMethod.PUT)
    @ApiOperation(value = "编辑报告任务")
    //编辑报告任务
    public ResultUtils updateReport(@RequestBody ReportCreationList reportCreationList){
        return reportCreationListService.updateReport(reportCreationList);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectAllReportByStaffCode",method = RequestMethod.GET)
    @ApiOperation(value = "查找这个学生所有的报告(未填，草稿，待审，通过的等)")
    //查找这个学生所有的报告
    public ResultUtils selectAllReportByStaffCode(){
        return reportCreationListService.selectAllReportByStaffCode();
    }

    //导入成员
    @UserLoginToken
    @ApiOperation(value = "导入成员",produces = "multipart/form-data")
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ResultUtils importExcel(MultipartFile file){
        List<ClassStudentInfo> memberDBS = FileUtil.importExcel(file, 0, 1, ClassStudentInfo.class,0);
        if(memberDBS==null || memberDBS.isEmpty()){
            return new ResultUtils(500,"导入失败/數據為空");
        }
        productionLineListService.updateStudentInfo(memberDBS);//存入数据库
        if(!memberDBS.isEmpty()){
            return new ResultUtils(101,"导入成功",memberDBS);
        }else if(memberDBS.isEmpty()){
            return new ResultUtils(101,"导入空数据",memberDBS);
        }else {
            return new ResultUtils(500,"导入失败");
        }
    }

    //导出(下载)模板
    @UserLoginToken
    @ApiOperation(value = "导出（下载）模板")
    @RequestMapping(value = "exportExcel",method = RequestMethod.GET)
    public void export(HttpServletResponse response) {
        List<ClassStudentInfo> memberDBS = new ArrayList<>();
        String fileName = "测试导出";
        //导出操作
        FileUtil.exportExcel(memberDBS,null,"Sheet1",ClassStudentInfo.class,fileName+ ".xlsx",response);
    }


    @UserLoginToken
    @RequestMapping(value = "/selectWhichLine",method = RequestMethod.GET)
    @ApiOperation(value = "查询学生在某个时间时在哪条产线")
    @ApiImplicitParam(name = "staffCode:学生工号;missionEndTime:任务结束时间")
    //查询学生在某个时间时在哪条产线
    public ResultUtils selectWhichLine(String staffCode, Date missionEndTime){
        return reportCreationListService.selectWhichLine(staffCode,missionEndTime);
    }

    //查询报告详细内容
    @UserLoginToken
    @RequestMapping(value = "/selectReportInfo",method = RequestMethod.GET)
    @ApiOperation(value = "查询报告详细内容")
    @ApiImplicitParam(name = "investFieldValue:对应表中字段值(就是report_id)")
    public ResultUtils selectReportInfo(Integer reportId,Integer reportType){
        return reportCreationListService.selectReportInfo(reportId,reportType);
    }

    //编辑报告详细内容
    @UserLoginToken
    @RequestMapping(value = "/updateReportByReportId",method = RequestMethod.PUT)
    @ApiOperation(value = "编辑产线报告详细内容")
    public ResultUtils updateReportByReportId(@RequestBody ReportProductionList reportProductionList) throws Exception{
        return reportCreationListService.updateReportByReportId(reportProductionList);
    }

    //查詢月報下所有週報的評分評語
    @UserLoginToken
    @RequestMapping(value = "/selectWeekApprovalInfo",method = RequestMethod.GET)
    @ApiOperation(value = "查詢月報下所有週報的評分評語")
    public ResultUtils selectWeekApprovalInfo(Integer missionType,Integer reportId,Integer creationId){
        return reportCreationListService.selectWeekApprovalInfo(missionType,reportId,creationId);
    }

    @UserLoginToken
    @RequestMapping(value = "/deleteMember",method = RequestMethod.PUT)
    @ApiOperation(value = "刪除成員管理的成員")
    public ResultUtils deleteMember(String staffCode){
        return reportCreationListService.deleteMember(staffCode);
    }

    @UserLoginToken
    @RequestMapping(value = "/updateProposalReport",method = RequestMethod.PUT)
    @ApiOperation(value = "編輯提案改善報告")
    public ResultUtils updateProposalReport(@RequestBody ReportProductionProposalList reportProductionProposalList) throws Exception{
        return reportCreationListService.updateProposalReport(reportProductionProposalList);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectTargetInfo",method = RequestMethod.POST)
    @ApiOperation(value = "填寫部門!週報!時查詢目標及任務列表")
    @ApiImplicitParam(name = "reportDepartList",value = "staffCode:\"學生工號;reportType:報告類型;missionStartTime:任務開始時間;missionEndTime:任務結束時間")
    public ResultUtils selectTargetInfo(@RequestBody ReportDepartList reportDepartList){
        return reportCreationListService.selectTargetInfo(reportDepartList);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectDailyByWeek",method = RequestMethod.GET)
    @ApiOperation(value = "查询周报下的日报内容")
    @ApiImplicitParam(name = "reportProductionList",value = "creationId:任务id")
    public ResultUtils selectDailyByWeek(Integer creationId){
        return reportCreationListService.selectDailyByWeek(creationId);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectMenuByRole",method = RequestMethod.GET)
    @ApiOperation(value = "根據角色查詢菜單下拉列表")
    public ResultUtils selectMenuByRole(){
        return reportCreationListService.selectMenuByRole();
    }

    @UserLoginToken
    @RequestMapping(value = "/selectClassPeriodList",method = RequestMethod.GET)
    @ApiOperation(value = "查詢屆別")
    public ResultUtils selectClassPeriodList(){
        return reportCreationListService.selectClassPeriodList();
    }

    //导入考勤
    @UserLoginToken
    @ApiOperation(value = "导入考勤",produces = "multipart/form-data")
    @RequestMapping(value = "/importAttendance",method = RequestMethod.POST)
    public ResultUtils importAttendance(MultipartFile file){
        List<AttendanceRecord> attendances = FileUtil.importExcel(file, 0, 1, AttendanceRecord.class,0);
        if(attendances==null){
            return new ResultUtils(500,"导入失败");
        } else if(attendances.isEmpty()){
            return new ResultUtils(101,"数据为空");
        }
        reportCreationListService.insertAttendance(attendances);
        if(!attendances.isEmpty()){
            return new ResultUtils(101,"导入成功",attendances);
        }else {
            return new ResultUtils(500,"导入失败");
        }
    }

    //导入獎懲
    @UserLoginToken
    @ApiOperation(value = "导入獎懲",produces = "multipart/form-data")
    @RequestMapping(value = "/importRewardsAndPunishment",method = RequestMethod.POST)
    public ResultUtils importRewardsAndPunishment(MultipartFile file){
        List<RewardPunishRecord> rewardPunishRecord = FileUtil.importExcel(file, 0, 1, RewardPunishRecord.class,0);
        if(rewardPunishRecord==null){
            return new ResultUtils(500,"导入失败");
        } else if(rewardPunishRecord.isEmpty()){
            return new ResultUtils(101,"数据为空");
        }

        reportCreationListService.importRewardsAndPunishment(rewardPunishRecord);
        if(!rewardPunishRecord.isEmpty()){
            return new ResultUtils(101,"导入成功",rewardPunishRecord);
        }else {
            return new ResultUtils(500,"导入失败");
        }
    }

    //导出考勤(下载)模板
    @UserLoginToken
    @ApiOperation(value = "导出考勤(下载)模板")
    @RequestMapping(value = "exportAttendance",method = RequestMethod.GET)
    public void exportAttendance(HttpServletResponse response) {
        List<AttendanceRecord> attendancess = new ArrayList<>();
        String fileName = "测试导出";
        //导出操作
        FileUtil.exportExcel(attendancess,null,"Sheet1",AttendanceRecord.class,fileName+ ".xlsx",response);
    }

    //导出奖惩(下载)模板
    @UserLoginToken
    @ApiOperation(value = "导出奖惩(下载)模板")
    @RequestMapping(value = "exportRewardsAndPunishment",method = RequestMethod.GET)
    public void exportRewardsAndPunishment(HttpServletResponse response) {
        List<RewardPunishRecord> rewardPunishRecordS = new ArrayList<>();
        String fileName = "测试导出";
        //导出操作
        FileUtil.exportExcel(rewardPunishRecordS,null,"Sheet1",RewardPunishRecord.class,fileName+ ".xlsx",response);
    }


    //查询奖惩记录
    @UserLoginToken
    @RequestMapping(value = "/queryRewardPunishRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询奖惩记录")
    @ApiImplicitParam(name = "queryRewardPunishRecord",required = true)
    public ResultUtils queryRewardPunishRecord(Integer pageNum) {
        return reportCreationListService.queryRewardPunishRecord(pageNum);
    }

    //查询考勤记录
    @UserLoginToken
    @RequestMapping(value = "/queryAttendanceRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询考勤记录")
    @ApiImplicitParam(name = "queryAttendanceRecord",required = true)
    public ResultUtils queryAttendanceRecord(Integer pageNum) {
        return reportCreationListService.queryAttendanceRecord(pageNum);
    }

    //編輯部門報告
    @UserLoginToken
    @RequestMapping(value = "/updateDepartReport",method = RequestMethod.PUT)
    @ApiOperation(value = "編輯部門報告")
    public ResultUtils updateDepartReport(@RequestBody ReportDepartList reportDepartList) throws Exception{
        return reportCreationListService.updateDepartReport(reportDepartList);
    }

/*
    @UserLoginToken
    @ApiOperation(value = "分页查询成员管理信息")
    @RequestMapping(value = "selectXiaoquByPage",method = RequestMethod.GET)
    //分页查询成员管理信息
    public ResultUtils selectXiaoquByPage(Integer pageNum){
        return reportCreationListService.selectMemberByPage(pageNum);
    }

/*
    //部門實習報告日報、週報、月報編輯
    @UserLoginToken
    @ApiOperation(value = "部門實習報告日報、週報、月報編輯")
    @RequestMapping(value = "/updateDepartReportInfo",method = RequestMethod.POST)
    public ResultUtils updateDepartReportInfo(@RequestBody ReportDepartList reportDepartList)throws Exception{
        return reportCreationListService.updateDepartReportInfo(reportDepartList);
    }*/




}
