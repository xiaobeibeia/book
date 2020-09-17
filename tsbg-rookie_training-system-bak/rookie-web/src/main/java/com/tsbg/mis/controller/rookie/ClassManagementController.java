package com.tsbg.mis.controller.rookie;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.model.ClassGradeInfo;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ClassGradeInfoVo;
import com.tsbg.mis.rookie.vo.ClassSquadListVo;
import com.tsbg.mis.rookie.vo.CommitteeVo;
import com.tsbg.mis.serviceImpl.rookie.ClassManagementService;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author F1337200
 **/
@RestController
@RequestMapping("/classManagement")
public class ClassManagementController {

    @Autowired
    private ClassManagementService classManagementService;

    //查詢菁干班所有成員(只传studentId时为查询单个成员详情)
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils selectAll(@RequestBody ClassStudentInfo classStudentInfo){
        return classManagementService.selectAll(classStudentInfo);
    }

    //查詢所有屆別
    @RequestMapping(value = "selectClassPeriod", method = RequestMethod.GET)
    @UserLoginToken
    public ResultUtils selectClassPeriod(){
        return classManagementService.selectClassPeriod();
    }

    //根據屆別查詢所有班級信息
    @RequestMapping(value = "/selectGradeInfo", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils selectGradeInfo(String classPeriod){
        return classManagementService.selectGradeInfo(classPeriod);
    }

    //班级管理-->設定班級
    @RequestMapping(value = "/placement", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils placement(@RequestBody List<ClassGradeInfo> classGradeInfo){
        return classManagementService.placement(classGradeInfo);
    }

    //根據id修改班級名稱  &  刪除班級
    @RequestMapping(value = "/modifyGrade", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils modifyGrade(@RequestBody ClassGradeInfo classGradeInfo){
        return classManagementService.modifyGrade(classGradeInfo);
    }

    //班级管理-->給班級添加成員  自動隨機分班
    @RequestMapping(value = "/addStudentIntoGradeAuto", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils addStudentIntoGrade(){
        return  classManagementService.addStudentIntoGradeAuto();
    }

    //手動分班（修改&微調）
    @RequestMapping(value = "/modifyStudentIntoGrade", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils modifyStudentIntoGrade(@RequestBody ClassGradeInfoVo classGradeInfoVo){
        return classManagementService.modifyStudentIntoGrade(classGradeInfoVo.getStaffCodes(),classGradeInfoVo.getGradeId());
    }

    //重置分班
    @RequestMapping(value = "/resetClassMembers", method = RequestMethod.GET)
    @UserLoginToken
    public ResultUtils resetClassMembers(){
        return classManagementService.resetClassMembers();
    }

    //查询class_student_info表中squadId = 0的（还未分组）
    @RequestMapping(value = "/selectStudentByGradeId", method = RequestMethod.GET)
    @UserLoginToken
    public ResultUtils selectStudentByGradeId(Integer gradeId){
        return classManagementService.selectStudentByGradeId(gradeId);
    }

    //班級成員分組  &  修改分組成員
    @RequestMapping(value = "/addStudentInfoSquad", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils addStudentInfoSquad(@RequestBody ClassSquadListVo classSquadListVo){
        return classManagementService.addStudentInfoSquad(classSquadListVo);
    }

    //查询当前届别中的某个班级下的各分组成员信息
    @RequestMapping(value = "/selectStudentByGradeAndSquad", method = RequestMethod.GET)
    @UserLoginToken
    public ResultUtils selectStudentByGradeAndSquad(Integer gradeId){
        return classManagementService.selectStudentByGradeAndSquad(gradeId);
    }


    //查询班级所设定的班委
    @RequestMapping(value = "/selectStudentCommittee", method = RequestMethod.GET)
    @UserLoginToken
    public ResultUtils selectStudentCommittee(Integer gradeId){
        return classManagementService.selectStudentCommittee(gradeId);
    }

    //修改班委信息
    @RequestMapping(value = "/modifyStudentCommittee", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils modifyStudentCommittee(@RequestBody CommitteeVo committeeVo){
        return classManagementService.modifyStudentCommittee(committeeVo);
    }

    //修改(刪除)組成員  &  删除整个分组
    @RequestMapping(value = "/modifySquadMembers", method = RequestMethod.POST)
    @UserLoginToken
    public ResultUtils modifySquadMembers(@RequestBody ClassGradeInfoVo classGradeInfoVo){
        return classManagementService.modifySquadMembers(classGradeInfoVo.getStaffCodes(),classGradeInfoVo.getSquadId());
    }

    //查询所有处级单位信息
    @RequestMapping(value = "/selectAllBuList", method = RequestMethod.GET)
    @UserLoginToken
    public  ResultUtils selectAllBuList(){
        return  classManagementService.selectAllBuList();
    }

}
