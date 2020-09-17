package com.tsbg.mis.controller.task;

import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.service.rookie.StudentTaskService;
import com.tsbg.mis.signed.bag.SignedPackage;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/task/student")
@Validated
@Api(value = "我的任务——菁干班", tags = "我的任务——菁干班")
public class StudentTaskController {

    private final StudentTaskService studentTaskService;

    public StudentTaskController(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    @ApiOperation(value = "提交产线报告", notes = "提交产线报告", httpMethod = "POST")
    @PostMapping("/submitReport")
    @UserLoginToken
    public ResultUtils submitDailyReportProduction(@RequestParam Integer reportId, @RequestParam Integer reportType, @RequestParam Integer lineId) {
        return studentTaskService.submitReport(reportId, reportType, lineId);
    }

    @ApiOperation(value = "提交提案改善报告", notes = "提交提案改善报告", httpMethod = "POST")
    @PostMapping("/improveReport")
    @UserLoginToken
    public ResultUtils improveReportProduction(@RequestParam Integer proposalId, @RequestParam Integer lineId) {
        return studentTaskService.improveReport(proposalId, lineId);
    }

    @ApiOperation(value = "提交部门报告", notes = "提交部门报告", httpMethod = "POST")
    @PostMapping("/submitDepartReport")
    @UserLoginToken
    public ResultUtils submitDepartReport(@RequestParam Integer reportId, @RequestParam Integer reportType) {
        return studentTaskService.submitDepartReport(reportId, reportType);
    }
}
