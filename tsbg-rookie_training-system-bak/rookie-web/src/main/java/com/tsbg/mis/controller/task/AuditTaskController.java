package com.tsbg.mis.controller.task;

import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.bag.MonthTargetApprovalInfoPackage;
import com.tsbg.mis.signed.bag.RookieApprovalInfoPackage;
import com.tsbg.mis.service.rookie.AuditTaskService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/task/audit")
@Validated
@Api(value = "审核任务", tags = "审核任务")
public class AuditTaskController {

    private final AuditTaskService auditTaskService;

    public AuditTaskController(AuditTaskService auditTaskService) {
        this.auditTaskService = auditTaskService;
    }


    //1
    @ApiOperation(value = "查询所有的审核任务", notes = "查询所有的审核任务", httpMethod = "GET")
    @GetMapping("/queryAll")
    @UserLoginToken
    public ResultUtils queryAll() {
        return auditTaskService.queryAll();
    }

    @ApiOperation(value = "查询所有报告类型", notes = "查询所有报告类型", httpMethod = "GET")
    @GetMapping("/inquireBusiness")
    @UserLoginToken
    public ResultUtils inquireBusiness() {
        return auditTaskService.inquireBusiness();
    }

    @ApiOperation(value = "产线主管签核报告", notes = "产线主管签核报告", httpMethod = "POST")
    @PostMapping("/audit")
    @UserLoginToken
    public ResultUtils auditReport(@RequestBody @Validated RookieApprovalInfoPackage rookieApprovalInfoPackage) {
        return auditTaskService.auditReport(rookieApprovalInfoPackage);
    }

    @ApiOperation(value = "人资签核部门主管的月目标", notes = "人资签核部门主管的月目标", httpMethod = "POST")
    @PostMapping("/auditMonthTarget")
    @UserLoginToken
    public ResultUtils auditMonthTarget(@RequestBody @Validated MonthTargetApprovalInfoPackage monthTargetApprovalInfoPackage) {
        return auditTaskService.auditMonthTarget(monthTargetApprovalInfoPackage);
    }

    @ApiOperation(value = "查询同一批次的所有月目标", notes = "查询同一批次的所有月目标", httpMethod = "POST")
    @PostMapping("/querySameMonthTarget/{targetNum}")
    @UserLoginToken
    public ResultUtils querySameMonthTarget(@PathVariable String targetNum) {
        return auditTaskService.querySameMonthTarget(targetNum);
    }

    @ApiOperation(value = "检查一组 targetNum 是否是同一个月目标", notes = "检查一组 targetNum 是否是同一个月目标", httpMethod = "POST")
    @PostMapping("/querySameMonthTarget")
    @UserLoginToken
    public ResultUtils checkSameMonthTarget(@RequestBody List<String> targetNumList) {
        return auditTaskService.checkSameMonthTarget(targetNumList);
    }

    @ApiOperation(value = "部门主管签核报告", notes = "部门主管签核报告", httpMethod = "POST")
    @PostMapping("/departAudit")
    @UserLoginToken
    public ResultUtils departAudit(@RequestBody @Validated RookieApprovalInfoPackage rookieApprovalInfoPackage) {
        return auditTaskService.departAudit(rookieApprovalInfoPackage);
    }
}
