package com.tsbg.mis.controller.departmentTarget;

import com.tsbg.mis.annotation.RequiredPerms;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.bag.MonthlyTargetPackage;
import com.tsbg.mis.service.rookie.WeekTargetService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/weekTarget")
@Api(value = "周目标管理", tags = "周目标管理")
public class WeekTargetController {

    private final WeekTargetService weekTargetService;

    public WeekTargetController(WeekTargetService weekTargetService) {
        this.weekTargetService = weekTargetService;
    }

    @ApiOperation(value = "查询月目标所有审核人", notes = "查询月目标所有审核人", httpMethod = "GET")
    @GetMapping("/queryReviewer")
    @UserLoginToken
    public ResultUtils queryReviewer() {
        return weekTargetService.queryReviewer();
    }

    /**
     * 创建完月目标，提交到人资去审核
     *
     * @param monthlyTargetPackage 月目标单号 审核人的 StaffCode
     * @return
     */
    @ApiOperation(value = "提交审核月目标", notes = "提交审核月目标", httpMethod = "POST")
    @PostMapping("/reviewMonthlyTarget")
    @UserLoginToken
    public ResultUtils reviewMonthlyTarget(@RequestBody @Validated MonthlyTargetPackage monthlyTargetPackage) {
        return weekTargetService.reviewMonthlyTarget(monthlyTargetPackage);
    }

    /**
     * 查询所有的月目标审核任务
     *
     * @return
     */
    @ApiOperation(value = "查询所有的月目标审核任务", notes = "查询所有的月目标审核任务", httpMethod = "GET")
    @GetMapping("/queryMonthTarget")
    @UserLoginToken
    public ResultUtils queryMonthTarget() {
        return weekTargetService.queryMonthTarget();
    }

    /**
     * 查询个人所有的目标（周和月）
     *
     * @return
     */
    @ApiOperation(value = "查询个人所有的目标（周和月）", notes = "查询个人所有的目标（周和月）", httpMethod = "GET")
    @GetMapping("/queryAllTarget")
    @UserLoginToken
    public ResultUtils queryAllTarget() {
        return weekTargetService.queryAllTarget();
    }
}
