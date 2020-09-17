package com.tsbg.mis.controller.productionLine;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ProductionLineInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineListVo;
import com.tsbg.mis.service.rookie.ProductionLineListService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productionLine")
@Api(value = "菁干班系统-产线管理",tags = "菁干班系统-产线管理")
public class ProductionLineListController {

    @Autowired
    ProductionLineListService productionLineListService;

    //新增产线
    @ApiOperation(value = "新增产线")
    @UserLoginToken
    @ApiImplicitParam(name = "productionLineListVo:产线对象",value = "传入参数为:\"lineName:产线名称;lineLeaderName:产线线长姓名;lineGroupLeaderName:产线组长姓名:lineManagerName:产线负责人姓名；lineLeaderCode:产线线长工号;lineGroupLeaderCode:产线组长工号:lineManagerCode:产线负责人工号；studentInfos:学生信息列表(一般只要id);internshipStartDate:实习开始时间;internshipEndDate:实习结束时间;")
    @RequestMapping(value = "/insertLine",method = RequestMethod.POST)
    public ResultUtils insertLine(@RequestBody ProductionLineListVo productionLineListVo){
        return productionLineListService.insertLine(productionLineListVo);
    }

    //模糊搜索产线
    @UserLoginToken
    @RequestMapping(value = "/fuzzySearchLine",method = RequestMethod.POST)
    @ApiOperation(value = "模糊搜索产线")
    @ApiImplicitParam(name = "productionLineListVo:产线对象",value = "传入参数为:\"lineName:产线名称;lineLeaderName:产线线长姓名;lineGroupLeaderName:产线组长姓名;lineManagerName:产线负责人姓名;internshipStartDate:实习开始时间;internshipEndDate:实习结束时间;staffCode:实习成员工号;staffName:实习成员姓名")
    public ResultUtils fuzzySearchLine(@RequestBody ProductionLineListVo productionLineListVo){
        return productionLineListService.fuzzySearchLine(productionLineListVo);
    }

    //编辑产线
    @UserLoginToken
    @RequestMapping(value = "/redactLine",method = RequestMethod.POST)
    @ApiOperation(value = "编辑产线")
    @ApiImplicitParam(name = "productionLineListVo:产线对象",value = "传入参数为\"lineId:产线id;studentInfos:学生信息集合(一般只要学生id集合);lineName:产线名称;lineLeaderName:产线线长名称;lineGroupLeaderName:产线组长名称;lineManagerName:产线负责人名称;")
    public ResultUtils redactLine(@RequestBody ProductionLineListVo productionLineListVo){
        return productionLineListService.redactLine(productionLineListVo);
    }

    //查询学生列表所有有效的学生
    @UserLoginToken
    @ApiOperation(value = "查询学生列表所有有效的学生")
    @RequestMapping(value = "/selectStudentInStudentInfo",method = RequestMethod.GET)
    public ResultUtils selectStudentInStudentInfo(){
        return productionLineListService.selectStudentInStudentInfo();
    }

    //查找所有user_info里的有效user
    @UserLoginToken
    @ApiOperation(value = "查找所有user_info里的有效user")
    @RequestMapping(value = "/selectUserInfo",method = RequestMethod.GET)
    public ResultUtils selectUserInfo(){
        return productionLineListService.selectUserInfo();
    }

    //删除产线(一并删除底下的成员
    @UserLoginToken
    @RequestMapping(value = "/deleteByLineId",method = RequestMethod.POST)
    @ApiOperation(value = "删除产线(一并删除底下的成员")
    @ApiImplicitParam(name = "lineId:产线id",value = "lineId:产线id")
    public ResultUtils deleteByLineId(Integer lineId){
        return productionLineListService.deleteByLineId(lineId);
    }

    //根据实习时间查找未分配的学生
    @UserLoginToken
    @RequestMapping(value = "/selectStudentWhereUndistributedByDate",method = RequestMethod.POST)
    @ApiOperation(value = "根据实习时间查找未分配的学生")
    @ApiImplicitParam(name = "productionLineListVo:产线对象",value = "传入参数为:\"internshipStartDate:实习开始时间;internshipEndDate:实习结束时间")
    public ResultUtils selectStudentWhereUndistributedByDate(@RequestBody ProductionLineListVo productionLineListVo){
        return productionLineListService.selectStudentWhereUndistributedByDate(productionLineListVo);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectStaffInfoByCode",method = RequestMethod.GET)
    @ApiOperation(value = "根据工号带出姓名")
    @ApiImplicitParam(name = "staffCode:工号",value = "传入参数为:staffCode:工号")
    public ResultUtils selectStaffInfoByCode(String staffCode){
        return productionLineListService.selectStaffInfoByCode(staffCode);
    }

    @UserLoginToken
    @RequestMapping(value = "/selectUserInfoByRole",method = RequestMethod.GET)
    @ApiOperation(value = "根据角色带出人员信息")
    public ResultUtils selectUserInfoByRole(Integer roleId){
        return productionLineListService.selectUserInfoByRole(roleId);
    }

    @UserLoginToken
    @RequestMapping(value = "/updateStudentInfo",method = RequestMethod.POST)
    @ApiOperation(value = "编辑学生信息")
    public ResultUtils updateStudentInfo(@RequestBody List<ClassStudentInfo> classStudentInfos){
        return productionLineListService.updateStudentInfo(classStudentInfos);
    }

    //查询实习产线记录详情
    @UserLoginToken
    @RequestMapping(value = "/queryProductionLineRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询产线实习记录详情")
    @ApiImplicitParam(name = "staffCode:学生工号",value = "staffCode:学生工号",required = true)
    public ResultUtils queryProductionLineRecord(@RequestBody ProductionLineInfoVo productionLineInfoVo){
        return productionLineListService.queryProductionLineRecord(productionLineInfoVo);
    }

    //查询部门实习记录详情
    @UserLoginToken
    @RequestMapping(value = "/querydepartmentInternshipRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询部门实习记录详情")
    @ApiImplicitParam(name = "staffCode:学生工号",value = "staffCode:学生工号",required = true)
    public ResultUtils querydepartmentInternshipRecord(String staffCode){
        return productionLineListService.querydepartmentInternshipRecord(staffCode);
    }

    //查询我的实习记录-产线实习记录
    @UserLoginToken
    @RequestMapping(value = "/queryMyProductionLineRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询我的实习记录-产线实习记录")
    @ApiImplicitParam(name = "productionLineInfoVo",value = "productionLineInfoVo",required = true)
    public ResultUtils queryMyProductionLineRecord(@RequestBody ProductionLineInfoVo productionLineInfoVo){
        return productionLineListService.queryMyProductionLineRecord(productionLineInfoVo);
    }

    //查询我的实习记录-部门实习记录
    @UserLoginToken
    @RequestMapping(value = "/queryMydepartmentInternshipRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询我的实习记录-部门实习记录")
    @ApiImplicitParam(name = "productionLineInfoVo",value = "productionLineInfoVo",required = true)
    public ResultUtils queryMydepartmentInternshipRecord(@RequestBody ProductionLineInfoVo productionLineInfoVo){
        return productionLineListService.queryMydepartmentInternshipRecord(productionLineInfoVo);
    }

    //查询我的实习记录-查询我的实习记录-部门、产线实习记录
    @UserLoginToken
    @RequestMapping(value = "/queryInternshipRecord",method = RequestMethod.POST)
    @ApiOperation(value = "查询我的实习记录-部门、产线实习记录")
    @ApiImplicitParam(name = "productionLineInfoVo",value = "productionLineInfoVo",required = true)
    public ResultUtils queryInternshipRecord(@RequestBody ProductionLineInfoVo productionLineInfoVo) throws Exception {
        return productionLineListService.queryInternshipRecord(productionLineInfoVo);
    }

}
