package com.tsbg.mis.controller.departmentTarget;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.bag.DepartTargetSetInfoPackage;
import com.tsbg.mis.rookie.bag.WeekDepartTargetPackage;
import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.model.DepartTargetList;
import com.tsbg.mis.rookie.model.ReportCreationList;
import com.tsbg.mis.service.rookie.DepartmentTargetService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departmentTarget")
@Api(value = "菁干班系统-部门（月、周）目标管理", tags = "菁干班系统-部门（月、周）目标管理")
@Validated
public class DepartmentTargetController {

    @Autowired
    DepartmentTargetService departmentTargetService;

    //查询所有类型
    @UserLoginToken
    @ApiOperation(value = "查询所有类型")
    @RequestMapping(value = "/queryAllType", method = RequestMethod.POST)
    public ResultUtils queryAllType() {
        return departmentTargetService.queryAllType();
    }

    //查询员工信息
    @UserLoginToken
    @ApiOperation(value = "查询员工信息")
    @RequestMapping(value = "/queryStaffInfo", method = RequestMethod.POST)
    public ResultUtils queryStaffInfo() {
        return departmentTargetService.queryStaffInfo();
    }

    //查询学生信息表中所有届别
    @UserLoginToken
    @ApiOperation(value = "查询学生信息表中所有届别")
    @RequestMapping(value = "/queryClassPeriod", method = RequestMethod.POST)
    public ResultUtils queryClassPeriod() {
        return departmentTargetService.queryClassPeriod();
    }

    //根据届别查询当前届别下的所有报告任务
    @UserLoginToken
    @ApiOperation(value = "根据届别查询当前届别下的所有报告任务")
    @RequestMapping(value = "/queryReportCreation", method = RequestMethod.POST)
    public ResultUtils queryReportCreation(String classPeriod) {
        return departmentTargetService.queryReportCreation(classPeriod);
    }

    //查询当前设立人所在事业处下的所有菁干班成员信息
    @UserLoginToken
    @ApiOperation(value = "根据届别查询当前设立人所在事业处下的所有菁干班成员信息")
    @RequestMapping(value = "/queryClassStudentInfo", method = RequestMethod.POST)
    public ResultUtils queryClassStudentInfo(@RequestBody ClassStudentInfo classStudentInfo) {
        return departmentTargetService.queryClassStudentInfo(classStudentInfo);
    }

    //设立部门月、周目标
    @UserLoginToken
    @ApiOperation(value = "设立部门月、周目标")
    @RequestMapping(value = "/insertDepartmentTarget", method = RequestMethod.POST)
    public ResultUtils insertDepartmentTargetInfo(@RequestBody DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        return departmentTargetService.insertDepartmentTargetInfo(departTargetSetInfoPackage);
    }

    //根据目标单号查询目标详情
    @UserLoginToken
    @ApiOperation(value = "根据目标单号查询目标详情")
    @RequestMapping(value = "/queryTargetDetailsInfo", method = RequestMethod.POST)
    public ResultUtils queryTargetDetailsInfo(@RequestBody DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        return departmentTargetService.queryTargetDetailsInfo(departTargetSetInfoPackage);
    }

    //部门月、周目标编辑
    /*@UserLoginToken
    @ApiOperation(value = "部门月、周目标编辑")
    @RequestMapping(value = "/updateTargetInfo", method = RequestMethod.POST)
    public ResultUtils updateTargetInfo(@RequestBody DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        return departmentTargetService.updateTargetInfo(departTargetSetInfoPackage);
    }*/

    //部门月目标查询
    @UserLoginToken
    @ApiOperation(value = "部门月目标查询")
    @RequestMapping(value = "/queryDepartmentMonthTargetInfo", method = RequestMethod.POST)
    public ResultUtils queryDepartmentMonthTargetInfo() {
        return departmentTargetService.queryDepartmentMonthTargetInfo();
    }

    //部门周目标查询
    @UserLoginToken
    @ApiOperation(value = "部门周目标查询")
    @RequestMapping(value = "/queryDepartmentWeekTargetInfo", method = RequestMethod.POST)
    public ResultUtils queryDepartmentWeekTargetInfo() {
        return departmentTargetService.queryDepartmentWeekTargetInfo();
    }

    //查询月报、周报
    @UserLoginToken
    @ApiOperation(value = "查询月报、周报")
    @RequestMapping(value = "/queryMonthlyWeekReportInfo", method = RequestMethod.POST)
    public ResultUtils queryMonthlyWeekReportInfo(@RequestBody ReportCreationList reportCreationList) {
        return departmentTargetService.queryMonthlyWeekReportInfo(reportCreationList);
    }

    //批量編輯目標
    @UserLoginToken
    @ApiOperation(value = "批量編輯目標")
    @RequestMapping(value = "/updateMonthlyTarget", method = RequestMethod.POST)
    public ResultUtils updateMonthlyTarget(@RequestBody DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception{
        return departmentTargetService.updateMonthlyTarget(departTargetSetInfoPackage);
    }

    //查询是否设立周目标
    @UserLoginToken
    @ApiOperation(value = "查询是否设立周目标")
    @RequestMapping(value = "/queryCreateTargetInfo", method = RequestMethod.POST)
    public ResultUtils queryCreateTargetInfo(@RequestBody WeekDepartTargetPackage weekDepartTargetPackage) {
        return departmentTargetService.queryCreateTargetInfo(weekDepartTargetPackage);
    }

    @UserLoginToken
    @ApiOperation(value = "单个创建周目标")
    @RequestMapping(value = "/createWeekTarget", method = RequestMethod.POST)
    public ResultUtils createWeekTarget(@RequestBody @Validated(Create.class) DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        return departmentTargetService.createWeekTarget(departTargetSetInfoPackage);
    }

    @UserLoginToken
    @ApiOperation(value = "修改周目标")
    @RequestMapping(value = "/updateWeekTarget", method = RequestMethod.PUT)
    public ResultUtils updateWeekTarget(@RequestBody @Validated(Update.class) DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception {
        return departmentTargetService.updateWeekTarget(departTargetSetInfoPackage);
    }
}
