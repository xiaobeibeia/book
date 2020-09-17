package com.tsbg.mis.controller.signed;

import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.group.Update;
import com.tsbg.mis.signed.bag.SignedPackage;
import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.service.rookie.SignedStreamService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 签核流程管理
 *
 * @author 汪永晖
 */
@RestController
@RequestMapping("/signed")
@Validated
@Api(value = "签核流程管理", tags = "签核流程管理")
public class SignedStreamController {

    private final SignedStreamService signedStreamService;


    public SignedStreamController(SignedStreamService signedStreamService) {
        this.signedStreamService = signedStreamService;
    }

    @ApiOperation(value = "新增签核流程", notes = "新增签核流程", httpMethod = "POST")
    @PostMapping("/insert")
    @UserLoginToken
    public ResultUtils insert(@RequestBody @Validated(Create.class) SignedPackage signedPackage) {
        return signedStreamService.insert(signedPackage);
    }

    @ApiOperation(value = "查询签核流程", notes = "查询签核流程", httpMethod = "GET")
    @GetMapping("/inquire")
    @UserLoginToken
    public ResultUtils inquire() {
        return signedStreamService.inquire();
    }

    @ApiOperation(value = "修改签核流程", notes = "修改签核流程", httpMethod = "PUT")
    @PutMapping("/update")
    @UserLoginToken
    public ResultUtils update(@RequestBody @Validated(Update.class) SignedPackage signedPackage) {
        return signedStreamService.update(signedPackage);
    }

    @ApiOperation(value = "删除签核流程", notes = "删除签核流程", httpMethod = "DELETE")
    @DeleteMapping("/delete/{businessId}")
    @UserLoginToken
    public ResultUtils delete(@PathVariable Integer businessId) {
        return signedStreamService.delete(businessId);
    }

    @ApiOperation(value = "根据ID查询签核流程", notes = "根据ID查询签核流程", httpMethod = "GET")
    @GetMapping("/selectBusiness/{businessId}")
    @UserLoginToken
    public ResultUtils selectBusiness(@PathVariable Integer businessId) {
        return signedStreamService.selectBusiness(businessId);
    }

    @ApiOperation(value = "根据报告ID查询签核进度", notes = "根据报告ID查询签核进度", httpMethod = "GET")
    @GetMapping("/selectApprovalInfo/{id}")
    @UserLoginToken
    public ResultUtils selectApprovalInfo(@PathVariable Integer id) {
        return signedStreamService.selectApprovalInfo(id);
    }

    @ApiOperation(value = "查询签核进度", notes = "查询签核进度", httpMethod = "POST")
    @PostMapping("/selectProgress")
    @UserLoginToken
    public ResultUtils selectProgress(@RequestParam(required = false) Integer reportId, @RequestParam Integer reportType) {
        return signedStreamService.selectProgress(reportId, reportType);
    }
}
