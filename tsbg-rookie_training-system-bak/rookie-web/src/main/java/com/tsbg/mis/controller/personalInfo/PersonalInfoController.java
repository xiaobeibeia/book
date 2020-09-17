package com.tsbg.mis.controller.personalInfo;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.bag.UserPersonalInfoPackage;
import com.tsbg.mis.service.rookie.PersonalInfoService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personalInfo")
@Api(value = "菁干班系统-查询、修改个人信息",tags = "菁干班系统-查询、修改个人信息")
public class PersonalInfoController {

    @Autowired
    PersonalInfoService personalInfoService;

    //查询我的组织信息
    @UserLoginToken
    @ApiOperation(value = "查询我的组织信息")
    @RequestMapping(value = "/queryOrganizationInfo",method = RequestMethod.POST)
    @ApiImplicitParam(name = "staffCode:学生工号",value = "staffCode:学生工号",required = true)
    public ResultUtils queryOrganizationInfo(String staffCode){
        return personalInfoService.queryOrganizationInfo(staffCode);
    }

    //修改个人信息
    @UserLoginToken
    @ApiOperation(value = "修改个人信息")
    @RequestMapping(value = "/UpdateUserPersonalInfo",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userPersonalInfoPackage", value = "修改个人信息内容:{\n" +
            "\"phoneNumber\":\"\",\n" +
            "\"emailAddress\":\"\",\n" +
            "\"fileName\":\"\",\n" +
            "\"filePath\":\"\",\n" +
            "\"viewPath\":\"\",\n" +
            "}", required = true)
    public ResultUtils UpdateUserPersonalInfo(@RequestBody UserPersonalInfoPackage userPersonalInfoPackage){
        return personalInfoService.UpdateUserPersonalInfo(userPersonalInfoPackage);
    }

    //查询个人信息、头像预览地址
    @UserLoginToken
    @ApiOperation(value = "查询个人信息、头像预览地址")
    @RequestMapping(value = "/queryPersonalInfo",method = RequestMethod.POST)
    public ResultUtils queryPersonalInfo(){
        return personalInfoService.selectStaffInfoByCode();
    }


}
